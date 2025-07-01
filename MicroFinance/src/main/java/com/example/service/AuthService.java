package com.example.service;

import com.example.model.User;

public interface AuthService {
	public User register(User user);
    public User login(String username, String password);
}
