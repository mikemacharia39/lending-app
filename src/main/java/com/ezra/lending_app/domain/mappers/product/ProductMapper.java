package com.ezra.lending_app.domain.mappers.product;

import com.ezra.lending_app.api.dto.ProductRequestDto;
import com.ezra.lending_app.api.dto.ProductResponseDto;
import com.ezra.lending_app.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductFeeMapper.class})
public abstract class ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    public abstract Product toEntity(ProductRequestDto requestDto);

    public abstract ProductResponseDto toDto(Product entity);
}
