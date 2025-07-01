package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.model.WalletTransaction;

public interface WalletTransactionRepository extends CrudRepository<WalletTransaction, Long>{
	List<WalletTransaction> findByWalletId(Long walletId);
	List<WalletTransaction> findByType(String type);
    List<WalletTransaction> findByStatus(String status);
}
