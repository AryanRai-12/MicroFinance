package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Loan;
import com.example.model.User;
import com.example.service.LoanService;
import com.example.service.UserService;

@Controller
@RequestMapping("/loans")
public class LoanController {
	@Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    // Show loan application form
    @GetMapping("/apply")
    public String showLoanForm(Model model) {
        model.addAttribute("loan", new Loan());
        return "loan-form"; // Thymeleaf view name (loan-form.html)
    }

    // Process loan application securely using logged-in user
    @PostMapping("/apply")
    public String submitLoanForm(@ModelAttribute Loan loan,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/error";
        }

        loan.setUser(user);
        loanService.applyForLoan(loan);
        return "redirect:/loans/my";
    }
    
    

    // Show only loans for the currently logged-in user
    @GetMapping("/my")
    public String viewMyLoans(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);

        List<Loan> loans = loanService.getLoansByUserId(user.getId());
        model.addAttribute("loans", loans);
        return "loan-list"; // Thymeleaf view name (loan-list.html)
    }

    // Admin only – approve a specific loan by ID
    @PostMapping("/approve/{loanId}")
    public String approveLoan(@PathVariable Long loanId) {
        loanService.approveLoan(loanId);
        return "redirect:/admin/loans";
    }
    
    
}
