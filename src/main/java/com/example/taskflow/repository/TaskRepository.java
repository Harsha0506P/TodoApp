package com.example.taskflow.repository;

import com.example.taskflow.entity.Task;
import com.example.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    List<Task> findByUserAndCompleted(User user, boolean completed);

    List<Task> findByUserAndDueDate(User user, LocalDate dueDate);

    List<Task> findByUserAndDueDateAfter(User user, LocalDate dueDate);

    List<Task> findByUserAndCompletedTrue(User user);

    long countByUser(User user);

    long countByUserAndCompletedFalse(User user);
}
