package com.ezra.lending_app.domain.mappers.customer;

import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.api.dto.customer.CustomerResponseDto;
import com.ezra.lending_app.domain.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CustomerAddressMapper.class})
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer customer);
}
