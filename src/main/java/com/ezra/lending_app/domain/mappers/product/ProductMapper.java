package com.ezra.lending_app.domain.mappers.product;

import com.ezra.lending_app.api.dto.product.ProductRequestDto;
import com.ezra.lending_app.api.dto.product.ProductResponseDto;
import com.ezra.lending_app.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductFeeMapper.class})
public abstract class ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "code", ignore = true)
    public abstract Product toEntity(ProductRequestDto requestDto);

    public abstract ProductResponseDto toDto(Product entity);
}
