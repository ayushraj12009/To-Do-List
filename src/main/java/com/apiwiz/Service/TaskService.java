package com.apiwiz.Service;

//import com.apiwiz.CustomException.TaskNotFoundException;
//import com.apiwiz.CustomException.UnauthorizedTaskAccessException;
//import com.apiwiz.CustomException.UserNotFoundException;
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



    public Task findById(Long id){
        return taskRepository.findById(id).get();
    }


    public Task findByIdOnlyAuthenticateUser(String email, Long id) throws Exception{
        User user = userRepository.findByEmail(email);
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()){
            throw new Exception("TaskList is not available with this ID");
        }

        if (!user.getTaskletDetails().contains(optionalTask.get())){
            throw  new Exception("This TaskList Item is not match with the user");
        }

        return optionalTask.get();
    }


    public void updateTaskStatus(Long taskId, String newStatus) throws Exception {

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            throw new Exception("Task not found with ID: " + taskId);
        }

        Task task = optionalTask.get();


        task.setStatus(newStatus);


        taskRepository.save(task);
    }








//    public String deleteTaskByIdOnlyAuthenticateUser(String email, Long taskId) throws TaskNotFoundException, UserNotFoundException, UnauthorizedTaskAccessException {
//        // Find the user by email
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new UserNotFoundException("User not found");
//        }
//
//        // Find the task by id
//        Optional<Task> optionalTask = taskRepository.findById(taskId);
//        if (optionalTask.isEmpty()) {
//            throw new TaskNotFoundException("Task not found");
//        }
//        Task task = optionalTask.get();
//
//        // Check if the task belongs to the user
//        if (!task.getUser().equals(user)) {
//            throw new UnauthorizedTaskAccessException("Task does not belong to the user");
//        }
//
//        // Delete the task from the user's task list
//        List<Task> userTaskList = user.getTaskletDetails();
//        userTaskList.remove(task);
//        userRepository.save(user);
//
//        // Delete the task from the database
//        taskRepository.deleteById(taskId);
//
//        return "Task deleted successfully";
//    }


    public String deleteByIdOnlyAuthenticateUser(String email, Long id) throws Exception {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found");
        }


        Optional<Task> optionalWishlist = taskRepository.findById(id);
        if (optionalWishlist.isEmpty()) {
            throw new Exception("Task  not found");
        }
        Task taskList = optionalWishlist.get();


        if (!taskList.getUser().equals(user)) {
            throw new Exception("Wishlist item does not belong to the user");
        }


        List<Task> userWishlists = user.getTaskletDetails();
        userWishlists.remove(taskList);
        user.setTaskletDetails(userWishlists);
        userRepository.save(user);


        taskRepository.deleteById(id);


        return "Wishlist item deleted successfully";
    }






}
