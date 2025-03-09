package com.ezra.lending_app.domain.repositories;

import com.ezra.lending_app.domain.entities.LoanRepaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepaymentReceiptRepository extends JpaRepository<LoanRepaymentReceipt, Long> {
    // find all loan repayment by loan id
    List<LoanRepaymentReceipt> findAllByLoanId(Long loanId);
}
