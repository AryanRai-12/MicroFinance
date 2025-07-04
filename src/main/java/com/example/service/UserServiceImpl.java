package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private UserRepository userRepository;

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User authenticate(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.filter(u -> u.getPassword().equals(password)).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Override
    public User findByEmail(String upiId) {
    	String upi = upiId;
        return userRepository.findByEmail(upi).orElse(null);
    }
    
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null); // ✅ Implementation
    }
}
