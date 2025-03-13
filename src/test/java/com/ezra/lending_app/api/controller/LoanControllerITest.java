package com.ezra.lending_app.api.controller;

import java.math.BigDecimal;

import com.ezra.lending_app.api.dto.customer.CustomerResponseDto;
import com.ezra.lending_app.api.dto.loan.LoanRequestDto;
import com.ezra.lending_app.api.dto.product.ProductResponseDto;
import com.ezra.lending_app.domain.enums.LoanTerm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanControllerITest extends BaseITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void checkLoanEligibilityTest() throws Exception {
        CustomerResponseDto customer = CustomerControllerITest.createCustomerUtil(mockMvc, objectMapper);
        ProductResponseDto product = ProductControllerITest.createProductUtil(mockMvc, objectMapper);

        // activate product before using to apply for loans
        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + product.getCode() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(MockMvcRequestBuilders.get("/loans/products/" + product.getCode() + "/customers/" + customer.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode").value(customer.getCode()))
                .andExpect(jsonPath("$.productCode").value(product.getCode()))
                .andExpect(jsonPath("$.requestedAmount").value("1000.0"));
    }

    @Test
    void applyForLoanTest() throws Exception {
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .requestedAmount(BigDecimal.valueOf(1000.00))
                .loanPeriod(7)
                .loanTerm(LoanTerm.DAY)
                .build();
        CustomerResponseDto customer = CustomerControllerITest.createCustomerUtil(mockMvc, objectMapper);
        ProductResponseDto product = ProductControllerITest.createProductUtil(mockMvc, objectMapper);

        // activate product before using to apply for loans
        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + product.getCode() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(MockMvcRequestBuilders.post("/loans/products/" + product.getCode() + "/customers/" + customer.getCode())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode").value(customer.getCode()))
                .andExpect(jsonPath("$.productCode").value(product.getCode()))
                .andExpect(jsonPath("$.requestedAmount").value("1000.0"))
                .andExpect(jsonPath("$.state").value("OPEN"));
    }

    @Test
    @DisplayName("Apply for loan and requested amount is greater than loan eligible")
    void applyForLoanAndRequestedAmountIsGreaterThanLoanEligibleTest() throws Exception {
        LoanRequestDto loanRequestDto = LoanRequestDto.builder()
                .requestedAmount(BigDecimal.valueOf(27000.00))
                .loanPeriod(7)
                .loanTerm(LoanTerm.DAY)
                .build();
        CustomerResponseDto customer = CustomerControllerITest.createCustomerUtil(mockMvc, objectMapper);
        ProductResponseDto product = ProductControllerITest.createProductUtil(mockMvc, objectMapper);

        // activate product before using to apply for loans
        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + product.getCode() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(MockMvcRequestBuilders.post("/loans/products/" + product.getCode() + "/customers/" + customer.getCode())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerCode").value(customer.getCode()))
                .andExpect(jsonPath("$.productCode").value(product.getCode()))
                .andExpect(jsonPath("$.requestedAmount").value("27000.0"))
                .andExpect(jsonPath("$.state").value("PENDING_APPROVAL"));

    }
}
