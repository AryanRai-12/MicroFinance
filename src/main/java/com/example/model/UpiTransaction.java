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
@Table(name = "upi_transactions")
public class UpiTransaction {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "1.00", message = "Minimum transaction amount is ₹1")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private BigDecimal amount;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status; // PENDING, SUCCESS, FAILED

    @NotBlank(message = "Sender UPI ID is required")
    @Column(nullable = false)
    private String senderUpi;

    @NotBlank(message = "Receiver UPI ID is required")
    @Column(nullable = false)
    private String receiverUpi;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public UpiTransaction() {
        this.timestamp = LocalDateTime.now();
    }

    public UpiTransaction(BigDecimal amount, String status, String senderUpi, String receiverUpi, User user) {
        this.amount = amount;
        this.status = status;
        this.senderUpi = senderUpi;
        this.receiverUpi = receiverUpi;
        this.timestamp = LocalDateTime.now();
        this.user = user;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSenderUpi() {
		return senderUpi;
	}

	public void setSenderUpi(String senderUpi) {
		this.senderUpi = senderUpi;
	}

	public String getReceiverUpi() {
		return receiverUpi;
	}

	public void setReceiverUpi(String receiverUpi) {
		this.receiverUpi = receiverUpi;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
    
}
