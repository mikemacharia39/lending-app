package com.ezra.lending_app.domain.services;

import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.api.dto.customer.CustomerResponseDto;
import com.ezra.lending_app.domain.entities.Customer;
import com.ezra.lending_app.domain.mappers.customer.CustomerMapper;
import com.ezra.lending_app.domain.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerResponseDto createCustomer(final CustomerRequestDto customerRequestDto) {
        final Customer customer = customerMapper.toEntity(customerRequestDto);
        final Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomer(final String customerCode) {
        final Customer customer = getCustomerEntity(customerCode);
        return customerMapper.toDto(customer);
    }

    @Transactional
    public Customer getCustomerEntity(final String customerCode) {
        final Optional<Customer> customer = customerRepository.findCustomerByCode(customerCode);
        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("The customer with code %s was not found", customerCode));
        }
        return customer.get();
    }
}
