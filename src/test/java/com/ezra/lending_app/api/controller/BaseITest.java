package com.ezra.lending_app.api.controller;

import com.ezra.lending_app.LendingApplication;
import com.ezra.lending_app.domain.repositories.CustomerRepository;
import com.ezra.lending_app.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = {"classpath:application-itest.properties"})
@SpringBootTest(classes = {LendingApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class BaseITest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        productRepository.deleteAll();
    }
}
