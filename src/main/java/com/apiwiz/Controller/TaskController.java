package com.apiwiz.Controller;


import com.apiwiz.DTO.GetAllUserTask;
import com.apiwiz.DTO.TaskDto;
import com.apiwiz.DTO.UpdateTaskStatusRequest;
import com.apiwiz.Model.Task;
import com.apiwiz.Model.User;
import com.apiwiz.Repository.TaskRepository;
import com.apiwiz.Repository.UserRepository;
import com.apiwiz.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class TaskController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthController authController;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;


    // This api is used to create a task
    @PostMapping("/createTask")
    public ResponseEntity createTaskByUser(@RequestBody TaskDto taskDto){
        try {
            // checking user is valid or not
            LogginRequest logginRequest = new LogginRequest(taskDto.getEmail(), taskDto.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            // if user is invalid then this will execute
            if(authResponse.getToken() == null || authResponse.getToken().isEmpty()){
                throw new UsernameNotFoundException("User Not Found");
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // If the user is valid, the task will be created as per the user's requirements and saved to the db
        User user = userRepository.findByEmail(taskDto.getEmail());

        Task taskItemCreated = new Task(
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getStatus(),
                taskDto.getDueDate()
        );

        taskItemCreated.setUser(user);

        Task createdItem = taskService.createTask(taskItemCreated);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }


    // This api is used to get all the task of user
    @GetMapping("/getAllUserTask")
    public List<Task> getUserTask(@RequestBody GetAllUserTask getAllUserTask) {

        try {
            // checking user is valid or not
            LogginRequest logginRequest = new LogginRequest(getAllUserTask.getEmail(), getAllUserTask.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            // if user is invalid then this will execute
            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return (List<Task>) stringResponseEntity;
        }

        // if user is valid then task will return
        return taskService.findAllTaskByUser(getAllUserTask.getEmail());
    }


    // this api is used to find a single task based on ID
    @GetMapping("/findTaskByID/{id}")
    public ResponseEntity findTaksByID(@PathVariable Long id){
        return ResponseEntity.ok(taskService.findById(id));
    }


    // this api is used to find authenticated user task
    @GetMapping("/findTaskAuthenticateUserById/{id}")
    public ResponseEntity findlist(@RequestBody GetAllUserTask userInfo, @PathVariable Long id) throws Exception {
        try {

            // checking user is valid or not
            LogginRequest logginRequest = new LogginRequest(userInfo.getEmail(), userInfo.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            // if user is invalid then this will execute
            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
        try {

            // if valid then this will return
            return ResponseEntity.ok(taskService.findByIdOnlyAuthenticateUser(userInfo.getEmail(), id));

        }catch (Exception e){

            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
    }


    // this api is used to update the task ur status means you completed or not or is ongoing etc...
    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateTaskStatus(@RequestBody UpdateTaskStatusRequest request) {
        try {

            // checking user is valid or not
            LogginRequest loginRequest = new LogginRequest(request.getEmail(), request.getPassword());
            AuthResponse authResponse = authController.signin(loginRequest);

            // if user is invalid then this will execute
            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }

            // if user is valid then task ur status will update
            taskService.updateTaskStatus(request.getTaskId(), request.getNewStatus(), request.getTaskUpdate());

            return ResponseEntity.ok("Task status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // this is additional features this api is used to for pagination
    @GetMapping("/getTaskList")
    public ResponseEntity<Page<Task>> getList(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) throws Exception {
        try {
            Page<Task> customerPage = taskRepository.findAll(PageRequest.of(page, size));
            return ResponseEntity.ok(customerPage);
        } catch (Exception e) {
            throw new Exception("Internal Server Issue");
        }
    }


    // this is additional features this api is used to sorting and filtering the data based on your conditions
    @GetMapping("/filterByStatus")
    public ResponseEntity<List<Task>> filterTasksByStatus(@RequestParam("status") String status,
                                                          @RequestParam(name = "sortBy", defaultValue = "dueDate") String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {
        try {
            // Validate status parameter
            if (!isValidStatus(status)) {
                return ResponseEntity.badRequest().body(null);
            }

            // Sorting the tasks based on the sortBy and sortOrder parameters
            Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

            // filtered tasks
            List<Task> filteredTasks = taskService.filterTasksByStatus(status, Sort.by(direction, sortBy));

            return ResponseEntity.ok(filteredTasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // method to validate status parameter
    private boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("Pending") ||
                status.equalsIgnoreCase("In Progress") ||
                status.equalsIgnoreCase("Completed");
    }


        // this api is used to delete the task
    @DeleteMapping("/deleteTaskAuthenticateUserById/{id}")
    public ResponseEntity deletelistByID(@RequestBody GetAllUserTask userInfo, @PathVariable Long id) throws Exception {
        try {

            // checking user is valid or not
            LogginRequest logginRequest = new LogginRequest(userInfo.getEmail(), userInfo.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            // if user is invalid then this will execute
            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
        try {

            // if user is valid then task will deleted
            return ResponseEntity.ok(taskService.deleteByIdOnlyAuthenticateUser(userInfo.getEmail(), id));

        }catch (Exception e){

            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
    }



}
