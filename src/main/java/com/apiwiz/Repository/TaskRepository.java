package com.apiwiz.Repository;

import com.apiwiz.Model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);
    Task findByIdAndUserId(Long taskId, Long userId);

    Page<Task> findAll(Pageable pageable);

    List<Task> findByStatus(String status, Sort sort);

}