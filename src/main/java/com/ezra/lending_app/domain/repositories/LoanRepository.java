package com.ezra.lending_app.domain.repositories;

import com.ezra.lending_app.domain.entities.Loan;
import com.ezra.lending_app.domain.enums.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByLoanCode(String loanCode);

    List<Loan> findAllByCustomerCode(String customerCode);

    List<Loan> findAllByStatusIn(List<LoanState> statuses);
}
