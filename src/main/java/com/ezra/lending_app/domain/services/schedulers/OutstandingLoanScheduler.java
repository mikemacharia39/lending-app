package com.ezra.lending_app.domain.services.schedulers;

import com.ezra.lending_app.domain.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OutstandingLoanScheduler {
    private LoanRepository loanRepository;

    @Transactional(readOnly = true)
    public void processOutstandingLoans() {

    }
}
