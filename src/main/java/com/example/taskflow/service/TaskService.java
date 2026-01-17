package com.example.taskflow.service;

import com.example.taskflow.entity.Task;
import com.example.taskflow.entity.User;
import com.example.taskflow.repository.TaskRepository;
import com.example.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    public void add(Task task) {
        task.setCompleted(false);
        repo.save(task);
    }

    public void markCompleted(Long id) {
        repo.findById(id).ifPresent(task -> {
            task.setCompleted(true);
            repo.save(task);
        });
    }

    public void deleteTask(Long id) {
        repo.deleteById(id);
    }

    public List<Task> all(User user) {
        return repo.findByUser(user);
    }

    public List<Task> todayTasks(User user) {
        return repo.findByUserAndDueDate(user, LocalDate.now());
    }

    public List<Task> upcomingTasks(User user) {
        return repo.findByUserAndDueDateAfter(user, LocalDate.now());
    }

    public List<Task> completedTasks(User user) {
        return repo.findByUserAndCompleted(user, true);
    }

    public long totalCount(User user) {
        return repo.findByUser(user).size();
    }

    public long completedCount(User user) {
        return repo.findByUserAndCompleted(user, true).size();
    }

    public long pendingCount(User user) {
        return totalCount(user) - completedCount(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
