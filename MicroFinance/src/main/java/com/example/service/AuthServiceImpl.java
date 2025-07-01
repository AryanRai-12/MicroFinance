package com.example.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.model.Wallet;
import com.example.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService{
	 @Autowired
	 private UserRepository userRepository;
	 @Autowired 
	 private WalletService walletService;
	 
	 @Autowired
	    private PasswordEncoder passwordEncoder;

	 @Override
	    public User register(User user) {
	        // ✅ Encrypt password before saving
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        User savedUser = userRepository.save(user);

	        // Create wallet and associate it
	        Wallet wallet = new Wallet();
	        wallet.setBalance(BigDecimal.ZERO);
	        wallet.setUser(savedUser);
	        walletService.createWallet(wallet);

	        savedUser.setWallet(wallet);
	        return userRepository.save(savedUser);
	    }

	    @Override
	    public User login(String username, String password) {
	        Optional<User> optionalUser = userRepository.findByUsername(username);
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            if (passwordEncoder.matches(password, user.getPassword())) {
	                return user;
	            }
	        }
	        return null;
	    }
	
}
