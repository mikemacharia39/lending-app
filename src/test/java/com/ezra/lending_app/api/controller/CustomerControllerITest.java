package com.ezra.lending_app.api.controller;

import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.api.dto.customer.CustomerResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.ezra.lending_app.api.controller.dto.MockUtil.mockCustomerRequestDto;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerITest extends BaseITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCustomer() throws Exception {
        CustomerRequestDto requestDto = mockCustomerRequestDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.identificationType").value("NATIONAL_ID"))
                .andExpect(jsonPath("$.identificationNumber").value("39387373"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.addresses[0].addressLine").value("The Muse 4th Floor"))
                .andExpect(jsonPath("$.addresses[0].town").value("Kikuyu"))
                .andExpect(jsonPath("$.addresses[0].state").value("Kiambu"))
                .andExpect(jsonPath("$.addresses[0].country").value("Kenya"))
                .andExpect(jsonPath("$.deviceId").value("device123"))
                .andExpect(jsonPath("$.deviceType").value("ANDROID"))
                .andExpect(jsonPath("$.preferredNotificationChannel").value("SMS"));
    }

    @Test
    void getCreatedCustomer() throws Exception {
        CustomerResponseDto responseDto = createCustomerUtil(mockMvc, objectMapper);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + responseDto.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.identificationType").value("NATIONAL_ID"))
                .andExpect(jsonPath("$.identificationNumber").value("39387373"));
    }

    public static CustomerResponseDto createCustomerUtil(final MockMvc mockMvc, final ObjectMapper objectMapper) throws Exception {
        final CustomerRequestDto requestDto = mockCustomerRequestDto();

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").isNotEmpty())
                .andReturn();

        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponseDto.class);
    }
}
