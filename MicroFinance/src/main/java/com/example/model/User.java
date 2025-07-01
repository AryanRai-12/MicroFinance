package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotBlank(message = "Password is required")
	@Size(min =8 , message = "Password must be at least 8 characters")
	@Column(nullable = false)
	private String password;
	 
	@NotBlank(message = "Username is required")
	@Column(unique = true, nullable = false)
	private String username;
	 
	@NotBlank(message = "Role is required")
	@Column(nullable = false)
	private String role = "USER"; 
	 
	@Email(message = "Invalid email")
	@Column(unique = true)
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number")
	private String mobile;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Wallet wallet;
	 
	@NotBlank(message = "UPI PIN is required")
	@Size(min = 4, max = 6, message = "UPI PIN must be 4–6 digits")
	@Column(name = "upi_pin", nullable = false)
	private String upiPin;

	public User() {}
	public User(Long id, String password, String username, String role, String email, String mobile, Wallet wallet, String upiPin) {
		
		this.id = id;
		this.password = password;
		this.username = username;
		this.role = "USER";
		this.email = email;
		this.mobile = mobile;
		this.wallet = wallet;
		this.upiPin=upiPin;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = "USER";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	public String getUpiPin() {
		return upiPin;
	}
	public void setUpiPin(String upiPin) {
		this.upiPin = upiPin;
	}	 	 	 
}
