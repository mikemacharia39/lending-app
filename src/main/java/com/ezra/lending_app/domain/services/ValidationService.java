package com.ezra.lending_app.domain.services;

import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.domain.entities.Product;
import com.ezra.lending_app.domain.enums.LoanState;
import com.ezra.lending_app.domain.enums.NotificationChannel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void validateCustomerRequest(CustomerRequestDto requestDto) {
        if (requestDto.preferredNotificationChannel() == NotificationChannel.EMAIL && requestDto.email() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required for email notifications");
        }
    }

    public void validateLoanApplicationRequest(Product product) {
        if (!product.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not active");
        }
    }
}
