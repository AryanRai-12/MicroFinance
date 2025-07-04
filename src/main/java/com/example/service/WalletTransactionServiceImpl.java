package com.example.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Wallet;
import com.example.model.WalletTransaction;
import com.example.repository.WalletTransactionRepository;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService{
	@Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public WalletTransaction recordTransaction(WalletTransaction transaction) {
        transaction.setTimestamp(java.time.LocalDateTime.now());
        return walletTransactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> getTransactionsForWallet(Long walletId) {
        return walletTransactionRepository.findByWalletId(walletId);
    }
    
    
}
