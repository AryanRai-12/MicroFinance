package com.example.repository;

import com.example.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {

    // 🔍 Optional: find wallet by user ID
    Optional<Wallet> findByUserId(Long userId);
}