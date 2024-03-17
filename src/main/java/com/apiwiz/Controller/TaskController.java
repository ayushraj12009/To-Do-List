package com.apiwiz.Controller;


import com.apiwiz.DTO.GetAllUserTask;
import com.apiwiz.DTO.TaskDto;
import com.apiwiz.DTO.UpdateTaskStatusRequest;
import com.apiwiz.Model.Task;
import com.apiwiz.Model.User;
import com.apiwiz.Repository.UserRepository;
import com.apiwiz.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
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


            taskService.updateTaskStatus(request.getTaskId(), request.getNewStatus());

            return ResponseEntity.ok("Task status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
