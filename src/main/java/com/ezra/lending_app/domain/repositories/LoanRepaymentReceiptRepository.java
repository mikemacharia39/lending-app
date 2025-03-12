package com.ezra.lending_app.domain.repositories;

import com.ezra.lending_app.domain.entities.LoanRepaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepaymentReceiptRepository extends JpaRepository<LoanRepaymentReceipt, Long> {

    @Query("SELECT lrr FROM LoanRepaymentReceipt lrr WHERE lrr.loan.id = :loanId")
    List<LoanRepaymentReceipt> findAllByLoanId(Long loanId);
}
