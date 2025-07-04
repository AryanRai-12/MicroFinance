package com.example.service;

import java.util.Optional;

import com.example.model.User;

public interface UserService {
	boolean existsByUsername(String username);
    User registerUser(User user);
    User authenticate(String username, String password);
    User findById(Long id);
    User findByEmail(String Email);
    User findByUsername(String username);
}
