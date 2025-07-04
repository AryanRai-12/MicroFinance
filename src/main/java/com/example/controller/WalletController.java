package com.example.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.UpiTransaction;
import com.example.model.User;
import com.example.model.Wallet;
import com.example.model.WalletTransaction;

import com.example.service.UserService;
import com.example.service.WalletService;
import com.example.service.WalletTransactionService;

@Controller
@RequestMapping("/wallet")
public class WalletController {
	@Autowired
    private WalletService walletService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @Autowired
    private UserService userService;
    
    

    //View wallet details by userId
    @GetMapping("/view")
    public String viewWallet(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        Wallet wallet = walletService.getWalletByUserId(user.getId());
        
        if (wallet == null) {
            model.addAttribute("error", "Wallet not found for user " + user.getUsername());
            return "error";
        }

        model.addAttribute("wallet", wallet);
        return "wallet-view";
    }

    //Show form to add money
    @GetMapping("/add")
    public String showAddForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        return "wallet-add";
    }


    //Handle add money request
    @PostMapping("/add")
    public String addMoney(@RequestParam("amount") double amount,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        Long userId = user.getId();

        Wallet updatedWallet = walletService.updateBalance(userId, amount, true);
        if (updatedWallet == null) {
            model.addAttribute("error", "Wallet not found or update failed.");
            return "error";
        }

        WalletTransaction transaction = new WalletTransaction();
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setType("ADD");
        transaction.setStatus("SUCCESS");
        transaction.setWallet(updatedWallet);
        walletTransactionService.recordTransaction(transaction);

        return "redirect:/wallet/view";
    }

    //Show form to deduct money
 // Show Withdraw Form
    @GetMapping("/withdraw")
    public String showWithdrawForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("userId", user.getId());
        return "wallet-withdraw"; // Use a dedicated page
    }

    // Handle Withdraw Submission
    @PostMapping("/withdraw")
    public String withdrawMoney(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("amount") double amount,
                                Model model) {
    	User user = userService.findByUsername(userDetails.getUsername());
        Wallet updatedWallet = walletService.updateBalance(user.getId(), amount, false);
		
		if (updatedWallet == null) { 
			model.addAttribute("error", "Insufficient funds or wallet not found."); 
			return "error"; 
		}
		 

        WalletTransaction transaction = new WalletTransaction();
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setType("WITHDRAW");
        transaction.setStatus("SUCCESS");
        transaction.setWallet(updatedWallet);
        walletTransactionService.recordTransaction(transaction);

        return "redirect:/wallet/view";
    }

    
    //View wallet transaction history
    @GetMapping("/transactions")
    public String viewTransactions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	User user=userService.findByUsername(userDetails.getUsername());
    	Wallet wallet=walletService.getWalletByUserId(user.getId());
        List<WalletTransaction> transactions = walletTransactionService.getTransactionsForWallet(wallet.getId());
        model.addAttribute("transactions", transactions);
        return "wallet-transaction-history";
    }
    
    
    
    
    @GetMapping("/transfer")
    public String showTransferForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User sender = userService.findByUsername(userDetails.getUsername());
        
        if (sender == null) {
            model.addAttribute("error", "User not authenticated");
            return "error";
        }

        model.addAttribute("senderUpi", sender.getUsername() + "@upi"); // for UI reference
        model.addAttribute("userId", sender.getId()); // if needed
        return "wallet-transfer"; // This should match your HTML file
    }

    
    
    @PostMapping("/transfer")
    public String transferMoney(@RequestParam String receiverUpi,
                                @RequestParam double amount,
                                @RequestParam String upiPin,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        User sender = userService.findByUsername(userDetails.getUsername());
        Wallet senderWallet = walletService.getWalletByUserId(sender.getId());
        // Check UPI PIN
        if (!sender.getUpiPin().equals(upiPin)) {
            model.addAttribute("error", "Invalid UPI PIN");
            WalletTransaction senderTransaction=new WalletTransaction(BigDecimal.valueOf(amount), "TRANSFER", "FAILED - invalid Upi pin", senderWallet);
        	walletTransactionService.recordTransaction(senderTransaction);
            return "redirect:/dashboard";
        }

        // Check balance
        
        if (senderWallet.getBalance().doubleValue() < amount) {
        	WalletTransaction senderTransaction=new WalletTransaction(BigDecimal.valueOf(amount), "TRANSFER", "FAILED - insufficient balance", senderWallet);
        	walletTransactionService.recordTransaction(senderTransaction);
            model.addAttribute("error", "Insufficient balance");
            return "redirect:/dashboard";
        }

        // Find receiver by UPI
        User receiver = userService.findByEmail(receiverUpi);
        
        if (receiver == null) {
        	WalletTransaction senderTransaction=new WalletTransaction(BigDecimal.valueOf(amount), "TRANSFER", "FAILED - invalid upi id", senderWallet);
        	walletTransactionService.recordTransaction(senderTransaction);
            model.addAttribute("error", "Receiver not found");
            return "redirect:/dashboard";
        }

        Wallet receiverWallet = walletService.getWalletByUserId(receiver.getId());

        // Transfer money
        walletService.updateBalance(sender.getId(), amount, false); // debit
        walletService.updateBalance(receiver.getId(), amount, true); // credit

        // Save transaction
        WalletTransaction reciverTransaction=new WalletTransaction(BigDecimal.valueOf(amount), "ADD", "SUCCESS", receiverWallet);
        WalletTransaction senderTransaction=new WalletTransaction(BigDecimal.valueOf(amount), "TRANSFER", "SUCCESS", senderWallet);
        
        walletTransactionService.recordTransaction(senderTransaction);
        walletTransactionService.recordTransaction(reciverTransaction);
        model.addAttribute("message", "Transfer successful");
        return "redirect:/dashboard";
    }

}
