package com.ezra.lending_app.api.dto;

public record CustomerAddressRequestDto(
        String addressLine,
        String town,
        String state,
        String country
) {
}
