package com.ezra.lending_app.domain.mappers.customer;

import com.ezra.lending_app.api.dto.CustomerAddressDto;
import com.ezra.lending_app.domain.entities.CustomerAddress;

public abstract class CustomerAddressMapper {
    abstract CustomerAddress toEntity(CustomerAddressDto customerAddressDto);
    abstract CustomerAddressDto toDto(CustomerAddress customerAddress);
}
