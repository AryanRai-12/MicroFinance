package com.example.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.model.Wallet;
import com.example.model.WalletTransaction;

public interface WalletTransactionService {
	WalletTransaction recordTransaction(WalletTransaction transaction);
    List<WalletTransaction> getTransactionsForWallet(Long walletId);
    
    
}
