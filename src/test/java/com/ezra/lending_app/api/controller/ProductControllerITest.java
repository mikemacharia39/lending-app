package com.ezra.lending_app.api.controller;

import com.ezra.lending_app.api.dto.product.ProductRequestDto;
import com.ezra.lending_app.api.dto.product.ProductResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.ezra.lending_app.api.controller.dto.MockUtil.mockProductRequestDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerITest extends BaseITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProductTest() throws Exception {
        ProductRequestDto requestDto = mockProductRequestDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Personal Loan"))
                .andExpect(jsonPath("$.description").value("A loan for personal use"))
                .andExpect(jsonPath("$.currency").value("KES"))
                .andExpect(jsonPath("$.minLoanAmount").value(1000))
                .andExpect(jsonPath("$.maxLoanAmount").value(50000))
                .andExpect(jsonPath("$.minLoanTermDuration").value(3))
                .andExpect(jsonPath("$.minLoanTermType").value("DAY"))
                .andExpect(jsonPath("$.maxLoanTermDuration").value(7))
                .andExpect(jsonPath("$.maxLoanTermType").value("DAY"))
                .andExpect(jsonPath("$.fees[0].name").value("Processing Fee"))
                .andExpect(jsonPath("$.fees[0].feeType").value("SERVICE_FEE"))
                .andExpect(jsonPath("$.fees[0].valueType").value("PERCENTAGE"))
                .andExpect(jsonPath("$.fees[0].value").value(1.5))
                .andExpect(jsonPath("$.repaymentFrequency").value("ONE_OFF"))
                .andExpect(jsonPath("$.gracePeriodAfterLoanDueDateInDays").value(10))
                .andExpect(jsonPath("$.gracePeriodBeforeLoanDueDateInDays").value(5));
    }

    @Test
    void getCreatedProductTest() throws Exception {
        ProductResponseDto productResponse = createProductUtil(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + productResponse.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Personal Loan"))
                .andExpect(jsonPath("$.description").value("A loan for personal use"));
    }

    @Test
    void activateProductTest() throws Exception {
        ProductResponseDto productResponse = createProductUtil(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + productResponse.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        mockMvc.perform(MockMvcRequestBuilders.put("/products/" + productResponse.getCode() + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    public static ProductResponseDto createProductUtil(final MockMvc mockMvc, final ObjectMapper objectMapper) throws Exception {
        ProductRequestDto requestDto = mockProductRequestDto();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProductResponseDto.class);
    }
}
