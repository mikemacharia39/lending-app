package com.ezra.lending_app.domain.services;

import com.ezra.lending_app.domain.enums.LoanState;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
public class ValidationService {

    private static final Map<LoanState, Set<LoanState>> validTransitions = new EnumMap<>(LoanState.class);

    static {
        validTransitions.put(LoanState.PENDING_APPROVAL, EnumSet.of(LoanState.OPEN, LoanState.CANCELLED, LoanState.REJECTED));
        validTransitions.put(LoanState.OPEN, EnumSet.of(LoanState.CLOSED, LoanState.WRITTEN_OFF));
        validTransitions.put(LoanState.CLOSED, EnumSet.noneOf(LoanState.class));
        validTransitions.put(LoanState.CANCELLED, EnumSet.noneOf(LoanState.class));
        validTransitions.put(LoanState.REJECTED, EnumSet.noneOf(LoanState.class));
        validTransitions.put(LoanState.WRITTEN_OFF, EnumSet.noneOf(LoanState.class));
    }

    public void validateLoanStateTransition(LoanState currentState, LoanState newState) {
        Set<LoanState> allowedTransitions = validTransitions.get(currentState);
        if (allowedTransitions == null || !allowedTransitions.contains(newState)) {
            throw new IllegalStateException("Invalid state transition from " + currentState + " to " + newState);
        }
    }
}
