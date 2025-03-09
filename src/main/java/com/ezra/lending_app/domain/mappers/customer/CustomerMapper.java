package com.ezra.lending_app.domain.mappers.customer;

import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.api.dto.customer.CustomerResponseDto;
import com.ezra.lending_app.domain.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {CustomerAddressMapper.class})
public abstract class CustomerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    public abstract Customer toEntity(CustomerRequestDto dto);

    public abstract CustomerResponseDto toDto(Customer customer);
}
