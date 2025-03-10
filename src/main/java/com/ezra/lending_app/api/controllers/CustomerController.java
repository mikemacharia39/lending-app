package com.ezra.lending_app.api.controllers;

import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.api.dto.customer.CustomerResponseDto;
import com.ezra.lending_app.domain.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RequestMapping("/customers")
@RestController
public class CustomerController {
    public final CustomerService customerService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        return customerService.createCustomer(customerRequestDto);
    }

    @GetMapping("/{customerCode}")
    public CustomerResponseDto getCustomer(@PathVariable String customerCode) {
        return customerService.getCustomer(customerCode);
    }
}
