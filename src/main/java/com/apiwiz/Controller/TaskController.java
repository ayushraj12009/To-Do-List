package com.apiwiz.Controller;


import com.apiwiz.DTO.GetAllUserTask;
import com.apiwiz.DTO.TaskDto;
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


            if(authResponse.getToken().isEmpty()){
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
            // Creating a login request object with email and password from the request body that is present in database
            LogginRequest logginRequest = new LogginRequest(getAllUserTask.getEmail(), getAllUserTask.getPassword());
            AuthResponse authResponse = authController.signin(logginRequest);

            if (authResponse.getToken().isEmpty()) {
                throw new UsernameNotFoundException("User Not Found");
            }
        } catch (Exception e) {
            ResponseEntity<String> stringResponseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return (List<Task>) stringResponseEntity;
        }
        // if login request is valid then return all the wishlist that is created by user
        return taskService.findAllTaskByUser(getAllUserTask.getEmail());
    }




}
