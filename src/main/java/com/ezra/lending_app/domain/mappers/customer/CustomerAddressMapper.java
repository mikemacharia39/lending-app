package com.ezra.lending_app.domain.mappers.customer;

import com.ezra.lending_app.api.dto.customer.CustomerAddressDto;
import com.ezra.lending_app.domain.entities.CustomerAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CustomerAddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    abstract CustomerAddress toEntity(CustomerAddressDto customerAddressDto);

    abstract CustomerAddressDto toDto(CustomerAddress customerAddress);
}
