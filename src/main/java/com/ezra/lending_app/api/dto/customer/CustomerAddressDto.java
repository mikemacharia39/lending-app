package com.ezra.lending_app.api.dto.customer;

import lombok.Builder;

@Builder
public record CustomerAddressDto(
        String addressLine,
        String town,
        String state,
        String country
) {
}
