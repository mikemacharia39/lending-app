package com.ezra.lending_app.domain.repositories;

import com.ezra.lending_app.domain.entities.Loan;
import com.ezra.lending_app.domain.enums.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByCode(String loanCode);

    @Query("SELECT l FROM Loan l WHERE l.customer.code = :customerCode")
    List<Loan> findAllLoansByCustomerCode(String customerCode);

    List<Loan> findAllByStateIn(List<LoanState> states);
}
