package com.ezra.lending_app.api.dto;

import lombok.Builder;

@Builder
public record CustomerAddressDto(
        String addressLine,
        String town,
        String state,
        String country
) {
}
