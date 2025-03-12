package com.ezra.lending_app.api.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerAddressDto {
    private String addressLine;
    private String town;
    private String state;
    private String country;
}
