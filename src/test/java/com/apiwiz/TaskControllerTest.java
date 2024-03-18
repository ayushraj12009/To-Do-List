package com.apiwiz;

import com.apiwiz.Controller.AuthController;
import com.apiwiz.Controller.AuthResponse;
import com.apiwiz.Controller.LogginRequest;
import com.apiwiz.Controller.TaskController;
import com.apiwiz.DTO.GetAllUserTask;
import com.apiwiz.DTO.TaskDto;
import com.apiwiz.Model.Task;
import com.apiwiz.Model.User;
import com.apiwiz.Repository.UserRepository;
import com.apiwiz.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void createTaskByUser_ValidRequest_ShouldReturnCreated() {
        // Arrange
        TaskDto taskDto = new TaskDto(
                "Task Title",
                "Task Description",
                "2024-03-25",
                "pending",
                "test@example.com",
                "password"
        );

        AuthResponse mockAuthResponse = new AuthResponse(taskDto.getEmail(),taskDto.getPassword());
        when(authController.signin(any(LogginRequest.class))).thenReturn(mockAuthResponse);

        User mockUser = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(mockUser);

        Task mockTask = new Task(
                "Task Title",
                "Task Description",
                "pending",
               "2024-03-25"
        );
        when(taskService.createTask(any(Task.class))).thenReturn(mockTask);

        // Act
        ResponseEntity responseEntity = taskController.createTaskByUser(taskDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockTask, responseEntity.getBody());
    }



    @Test
    public void getAllWishListByUser_ValidRequest_ShouldReturnTasks() {
        // Arrange
        GetAllUserTask getAllUserTask = new GetAllUserTask("test@example.com", "password");

        AuthResponse mockAuthResponse = new AuthResponse(getAllUserTask.getEmail(), getAllUserTask.getPassword());
        when(authController.signin(any(LogginRequest.class))).thenReturn(mockAuthResponse);

        List<Task> mockTasks = new ArrayList<>();
        // Add mock tasks as needed
        when(taskService.findAllTaskByUser(anyString())).thenReturn(mockTasks);

        // Act
        List<Task> result = taskController.getAllWishListByUser(getAllUserTask);

        // Assert
        assertEquals(mockTasks, result);
    }


    @Test
    public void getAllWishListByUser_InvalidRequest_ShouldReturnBadRequest() {
        // Arrange
        GetAllUserTask getAllUserTask = new GetAllUserTask("test@example.com", "password");

        AuthResponse mockAuthResponse = new AuthResponse(getAllUserTask.getEmail(), getAllUserTask.getPassword());
        // Assume the authentication response is invalid or empty
        when(authController.signin(any(LogginRequest.class))).thenReturn(mockAuthResponse);

        // Act
        List<Task> result = taskController.getAllWishListByUser(getAllUserTask);

        // Assert
        // Expecting the result to be an empty list due to failed authentication
        assertEquals(new ArrayList<>(), result);
    }



    @Test
    public void findTaskByID_ValidID_ShouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task mockTask = new Task("Task Title", "Task Description", "pending", "2024-03-25");
        when(taskService.findById(anyLong())).thenReturn(mockTask);

        // Act
        ResponseEntity responseEntity = taskController.findTaksByID(taskId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockTask, responseEntity.getBody());
    }

















}
