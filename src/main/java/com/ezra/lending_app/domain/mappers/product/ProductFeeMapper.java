package com.ezra.lending_app.domain.mappers.product;

import com.ezra.lending_app.api.dto.product.ProductFeeDto;
import com.ezra.lending_app.domain.entities.ProductFee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ProductFeeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    @Mapping(target = "product", ignore = true)
    abstract ProductFee toEntity(ProductFeeDto productFeeDto);

    abstract ProductFeeDto toDto(ProductFee productFee);
}
