package com.apiwiz.Service;

//import com.apiwiz.CustomException.TaskNotFoundException;
//import com.apiwiz.CustomException.UnauthorizedTaskAccessException;
//import com.apiwiz.CustomException.UserNotFoundException;
import com.apiwiz.Model.Task;
import com.apiwiz.Model.User;
import com.apiwiz.Repository.TaskRepository;
import com.apiwiz.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    public void updateTaskStatus(Long taskId, String newStatus, String taskUpdate) throws Exception {

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            throw new Exception("Task not found with ID: " + taskId);
        }

        Task task = optionalTask.get();


        task.setStatus(newStatus);
        task.setDescription(taskUpdate);


        taskRepository.save(task);
    }

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
            throw new Exception("Task does not belong to the user");
        }


        List<Task> userWishlists = user.getTaskletDetails();
        userWishlists.remove(taskList);
        user.setTaskletDetails(userWishlists);
        userRepository.save(user);


        taskRepository.deleteById(id);


        return "Task deleted successfully";
    }


    public Page<Task> getAllCustomers(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public List<Task> filterTasksByStatus(String status, Sort sort) {
        return taskRepository.findByStatus(status, sort);
    }













}
