package com.example.controller;

import com.example.model.User;
import com.example.model.Wallet;
import com.example.model.Loan;
import com.example.model.WalletTransaction;
import com.example.service.UserService;
import com.example.service.LoanService;
import com.example.service.WalletService;
import com.example.service.WalletTransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @GetMapping("/dashboard")
    public String showDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        Wallet wallet = walletService.getWalletByUserId(user.getId());
        List<Loan> loans = loanService.getLoansByUserId(user.getId());
        List<WalletTransaction> transactions = walletTransactionService.getTransactionsForWallet(wallet.getId()).stream()
                .sorted(Comparator.comparing(WalletTransaction::getTimestamp).reversed())
                .limit(10)
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("wallet", wallet);
        model.addAttribute("loans", loans);
        model.addAttribute("transactions", transactions);

        return "dashboard"; // points to dashboard.html (Thymeleaf view)
    }
}
