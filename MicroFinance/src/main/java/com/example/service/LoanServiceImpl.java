package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Loan;
import com.example.repository.LoanRepository;

@Service
public class LoanServiceImpl implements LoanService {
	@Autowired
    private LoanRepository loanRepository;

    @Override
    public Loan applyForLoan(Loan loan) {
        loan.setStatus("PENDING");
        return loanRepository.save(loan);
    }

    @Override
    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    @Override
    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan != null) {
            loan.setStatus("APPROVED");
            return loanRepository.save(loan);
        }
        return null;
    }
}
