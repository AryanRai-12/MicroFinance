package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	 @Autowired
	    private AuthService authService;

	    @Autowired
	    private UserService userService;
	    
	    
	    
	    
	    
	    
	    @GetMapping("/login")
	    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
	                                @RequestParam(value = "logout", required = false) String logout,
	                                Model model) {
	        if (error != null) {
	            model.addAttribute("error", "Invalid username or password");
	        }
	        if (logout != null) {
	            model.addAttribute("message", "You have been logged out successfully.");
	        }
	        return "login"; // login.html (Thymeleaf)
	    }

	    @GetMapping("/register")
	    public String showRegistrationForm(Model model) {
	        model.addAttribute("user", new User());
	        return "register"; // register.html (Thymeleaf)
	    }

	    @PostMapping("/register")
	    public String register(@ModelAttribute("user") User user, Model model) {
	        boolean exists = userService.existsByUsername(user.getUsername());

	        if (exists) {
	            model.addAttribute("error", "Username already taken");
	            return "register";
	        }

	        authService.register(user);
	        return "redirect:/auth/login";
	    }
}
