package com.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "loans")
public class Loan {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@DecimalMin(value = "2000.00", message = "Minimum loan amount is ₹2000")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private BigDecimal amount;
	
	 @NotNull(message = "Due date is required")
	 private LocalDate dueDate;
	 
	 @NotBlank(message = "Status is required")
	 @Column(nullable = false)
	 private String status; // PENDING, APPROVED, REPAID, REJECTED

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "user_id", nullable = false)
	 private User user;
	 
	 public Loan() {}
	 
	 public Loan(BigDecimal amount, LocalDate dueDate, String status, User user) {
	        this.amount = amount;
	        this.dueDate = dueDate;
	        this.status = status;
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

	 public LocalDate getDueDate() {
		 return dueDate;
	 }

	 public void setDueDate(LocalDate dueDate) {
		 this.dueDate = dueDate;
	 }

	 public String getStatus() {
		 return status;
	 }

	 public void setStatus(String status) {
		 this.status = status;
	 }

	 public User getUser() {
		 return user;
	 }

	 public void setUser(User user) {
		 this.user = user;
	 }
	 
}
