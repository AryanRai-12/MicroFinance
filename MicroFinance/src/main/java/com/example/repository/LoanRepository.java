package com.example.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Loan;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {
	List<Loan> findByUserId(Long userId);
	List<Loan> findByStatus(String status);
	

}
