package com.example.service;

import java.util.List;

import com.example.model.Loan;

public interface LoanService {
	Loan applyForLoan(Loan loan);
    List<Loan> getLoansByUserId(Long userId);
    Loan approveLoan(Long loanId);
}
