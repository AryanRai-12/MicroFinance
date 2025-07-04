package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.model.Wallet;
import com.example.repository.UserRepository;
import com.example.repository.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService{
	@Autowired
    private WalletRepository walletRepository;
	
	@Autowired
	private UserRepository userRepository;

    @Override
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
    
    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet updateBalance(Long userId, double amount, boolean isCredit) {
        Wallet wallet = getWalletByUserId(userId);
        if (wallet != null) {
            double current = wallet.getBalance().doubleValue();
            double updated = isCredit ? current + amount : current - amount;
            wallet.setBalance(new java.math.BigDecimal(updated));
            return walletRepository.save(wallet);
        }
        return null;
    }
    
    
}
