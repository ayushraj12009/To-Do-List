package com.apiwiz.Service;

import com.apiwiz.Model.Task;
import com.apiwiz.Model.User;
import com.apiwiz.Repository.TaskRepository;
import com.apiwiz.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    public Task createTask(Task task) {
        return taskRepository.save(task);
    }


    public List<Task> findAllTaskByUser(String email){
        User user = userRepository.findByEmail(email);
        return user.getTaskletDetails();
    }

//    public Task findById(Long id){
//        return taskRepository.findById(id).get();
//    }
//
//
//    public Task findByIdOnlyAuthenticateUser(String email, Long id) throws Exception{
//        User user = userRepository.findByEmail(email);
//        Optional<Task> optionalTask = taskRepository.findById(id);
//
//        if (optionalTask.isEmpty()){
//            throw new Exception("TaskList is not available with this ID");
//        }
//
//        if (!user.getTaskletDetails().contains(optionalTask.get())){
//            throw  new Exception("This TaskList Item is not match with the user");
//        }
//
//        return optionalTask.get();
//
//    }
//
//
//    public String deleteByIdOnlyAuthenticateUser(String email, Long id) throws Exception {
//        // Find the user by email
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new Exception("User not found");
//        }
//
//        // Find the wishlist item by id
//        Optional<Task> OptionalTask = taskRepository.findById(id);
//        if (OptionalTask.isEmpty()) {
//            throw new Exception("Task item not found");
//        }
//        Task task = OptionalTask.get();
//
//        // Check if the task item belongs to the user
//        if (!task.getUser().equals(user)) {
//            throw new Exception("Wishlist item does not belong to the user");
//        }
//
//        List<Task> taskList = user.getTaskletDetails();
//        taskList.remove(task);
//        user.setTaskletDetails(taskList);
//        userRepository.save(user);
//
//
//        // Delete the wishlist item from the database
//        userRepository.deleteById(id);
//
//        // I have to work on it
//        return "Wishlist item deleted successfully";
//    }
//
//






}
