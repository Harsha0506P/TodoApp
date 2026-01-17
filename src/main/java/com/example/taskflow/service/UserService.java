package com.example.taskflow.service;

import com.example.taskflow.entity.User;
import com.example.taskflow.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean register(User user) {
        if (repo.findByEmail(user.getEmail()) != null) {
            return false;
        }
        repo.save(user);
        return true;
    }
}
