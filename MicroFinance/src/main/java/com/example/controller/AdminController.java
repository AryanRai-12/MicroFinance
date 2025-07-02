package com.example.controller;

import com.example.model.Loan;
import com.example.model.User;
import com.example.model.WalletTransaction;
import com.example.repository.LoanRepository;
import com.example.repository.WalletTransactionRepository;
import com.example.service.LoanService;
import com.example.service.UserService;
import com.example.service.WalletTransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private WalletTransactionService transactionService;
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private WalletTransactionRepository transactionRepository;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	model.addAttribute("username", userDetails.getUsername());
        return "admin-dashboard"; 
    }
    
    @GetMapping("/allTransaction")
    public String showAllTransaction(Model model) {
        model.addAttribute("transactionList", transactionRepository.findAll());
        return "all-transactions";
    }

    @GetMapping("/pendingloans")
    public String viewPendingLoans(Model model) {
        List<Loan> pendingLoans = loanRepository.findByStatus("PENDING");
        model.addAttribute("pendingLoans", pendingLoans);
        return "admin-pending-loans";
    }

    @PostMapping("/processLoans")
    public String processSelectedLoans(@RequestParam(name = "loanIds", required = false) List<Long> loanIds,
                                       @RequestParam("action") String action) {

        if (loanIds != null && !loanIds.isEmpty()) {
            Iterable<Loan> loans = loanRepository.findAllById(loanIds);

            for (Loan loan : loans) {
                if ("approve".equalsIgnoreCase(action)) {
                    loan.setStatus("APPROVED");
                } else if ("reject".equalsIgnoreCase(action)) {
                    loan.setStatus("REJECTED");
                }
            }

            loanRepository.saveAll(loans);
        }

        return "redirect:/pendingloans";
    }

    
    
    
}
