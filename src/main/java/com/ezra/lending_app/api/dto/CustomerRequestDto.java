package com.ezra.lending_app.api.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CustomerRequestDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        List<CustomerAddressRequestDto> addresses
) {
}
