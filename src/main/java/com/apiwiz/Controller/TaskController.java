package com.apiwiz.Controller;


import com.apiwiz.DTO.FilterByStatus;
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


    @PostMapping("/createTask")
    public ResponseEntity createTaskByUser(@RequestBody TaskDto taskDto){
        try {

            LogginRequest logginRequest = new LogginRequest(taskDto.getEmail(), taskDto.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);


            if(authResponse.getToken() == null || authResponse.getToken().isEmpty()){
                throw new UsernameNotFoundException("User Not Found");
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

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


    @GetMapping("/getAllUserTask")
    public List<Task> getAllWishListByUser(@RequestBody GetAllUserTask getAllUserTask) {

        try {

            LogginRequest logginRequest = new LogginRequest(getAllUserTask.getEmail(), getAllUserTask.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return (List<Task>) stringResponseEntity;
        }

        return taskService.findAllTaskByUser(getAllUserTask.getEmail());
    }


    @GetMapping("/findTaskByID/{id}")
    public ResponseEntity findTaksByID(@PathVariable Long id){
        return ResponseEntity.ok(taskService.findById(id));
    }




    @GetMapping("/findTaskAuthenticateUserById/{id}")
    public ResponseEntity findlist(@RequestBody GetAllUserTask userInfo, @PathVariable Long id) throws Exception {
        try {

            LogginRequest logginRequest = new LogginRequest(userInfo.getEmail(), userInfo.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
        try {

            return ResponseEntity.ok(taskService.findByIdOnlyAuthenticateUser(userInfo.getEmail(), id));

        }catch (Exception e){

            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
    }



    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateTaskStatus(@RequestBody UpdateTaskStatusRequest request) {
        try {

            LogginRequest loginRequest = new LogginRequest(request.getEmail(), request.getPassword());
            AuthResponse authResponse = authController.signin(loginRequest);

            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }


            taskService.updateTaskStatus(request.getTaskId(), request.getNewStatus(), request.getTaskUpdate());

            return ResponseEntity.ok("Task status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



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



    @GetMapping("/filterByStatus")
    public ResponseEntity<List<Task>> filterTasksByStatus(@RequestParam("status") String status,
                                                          @RequestParam(name = "sortBy", defaultValue = "dueDate") String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {
        try {
            // Validate status parameter
            if (!isValidStatus(status)) {
                return ResponseEntity.badRequest().body(null);
            }

            // Sort the tasks based on the sortBy and sortOrder parameters
            Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

            // Get the filtered tasks
            List<Task> filteredTasks = taskService.filterTasksByStatus(status, Sort.by(direction, sortBy));

            return ResponseEntity.ok(filteredTasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Helper method to validate status parameter
    private boolean isValidStatus(String status) {
        return status.equalsIgnoreCase("Pending") ||
                status.equalsIgnoreCase("In Progress") ||
                status.equalsIgnoreCase("Completed");
    }



    @DeleteMapping("/deleteTaskAuthenticateUserById/{id}")
    public ResponseEntity deletelistByID(@RequestBody GetAllUserTask userInfo, @PathVariable Long id) throws Exception {
        try {

            LogginRequest logginRequest = new LogginRequest(userInfo.getEmail(), userInfo.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            if (authResponse.getToken() == null || authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
        try {

            return ResponseEntity.ok(taskService.deleteByIdOnlyAuthenticateUser(userInfo.getEmail(), id));

        }catch (Exception e){

            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return stringResponseEntity;
        }
    }



}
