package com.example.service;


import com.example.model.Wallet;

public interface WalletService {
	Wallet getWalletByUserId(Long userId);
    Wallet createWallet(Wallet wallet);
    Wallet save(Wallet wallet);
    Wallet updateBalance(Long userId, double amount, boolean isCredit);
    
}
