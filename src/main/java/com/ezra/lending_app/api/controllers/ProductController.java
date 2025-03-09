package com.ezra.lending_app.api.controllers;

import com.ezra.lending_app.api.dto.product.ProductRequestDto;
import com.ezra.lending_app.api.dto.product.ProductResponseDto;
import com.ezra.lending_app.domain.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProductResponseDto createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @GetMapping("/{productCode}")
    public ProductResponseDto getProduct(@PathVariable String productCode) {
        return productService.getProduct(productCode);
    }

    @PutMapping("/{productCode}/activate")
    public ProductResponseDto activateProduct(@PathVariable String productCode) {
        return productService.activateProduct(productCode);
    }

    @DeleteMapping("/{productCode}")
    public ProductResponseDto deactivateProduct(@PathVariable String productCode) {
        return productService.deactivateProduct(productCode);
    }
}
