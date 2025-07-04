package com.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "0.01", message = "Amount must be at least ₹0.01")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private BigDecimal amount;

    @NotBlank(message = "Transaction type is required")
    @Column(nullable = false)
    private String type; // e.g. ADD, CASHBACK, SYSTEM_ADJUST, REFUND

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status; // SUCCESS, PENDING, FAILED

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

   
    public WalletTransaction() {
        this.timestamp = LocalDateTime.now();
    }

    public WalletTransaction(BigDecimal amount, String type, String status, Wallet wallet) {
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.wallet = wallet;
        this.timestamp = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
    
    
}
