package com.ezra.lending_app.domain.services;

import com.ezra.lending_app.api.dto.product.ProductRequestDto;
import com.ezra.lending_app.api.dto.product.ProductResponseDto;
import com.ezra.lending_app.domain.entities.Product;
import com.ezra.lending_app.domain.mappers.product.ProductMapper;
import com.ezra.lending_app.domain.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto createProduct(final ProductRequestDto productRequestDto) {
        final Product product = productMapper.toEntity(productRequestDto);
        final Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(final String productCode) {
        Product product = getProductEntity(productCode);
        return productMapper.toDto(product);
    }

    public ProductResponseDto activateProduct(final String productCode) {
        Product product = getProductEntity(productCode);
        product.activate();
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Transactional
    public ProductResponseDto deactivateProduct(final String productCode) {
        Product product = getProductEntity(productCode);
        product.deactivate();
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public Product getProductEntity(final String productCode) {
        final Optional<Product> productOpt = productRepository.findByProductCode(productCode);
        if (productOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("The product with code %s was not found", productCode));
        }
        return productOpt.get();
    }
}
