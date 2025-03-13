package com.ezra.lending_app.api.controller.dto;

import com.ezra.lending_app.api.dto.customer.CustomerAddressDto;
import com.ezra.lending_app.api.dto.customer.CustomerRequestDto;
import com.ezra.lending_app.api.dto.product.ProductFeeDto;
import com.ezra.lending_app.api.dto.product.ProductRequestDto;
import com.ezra.lending_app.domain.enums.DeviceOSType;
import com.ezra.lending_app.domain.enums.FeeType;
import com.ezra.lending_app.domain.enums.IdentificationType;
import com.ezra.lending_app.domain.enums.LoanFeeValueType;
import com.ezra.lending_app.domain.enums.LoanTerm;
import com.ezra.lending_app.domain.enums.NotificationChannel;
import com.ezra.lending_app.domain.enums.ProductLoanTenure;
import com.ezra.lending_app.domain.enums.RepaymentFrequencyType;

import java.math.BigDecimal;
import java.util.List;

public class MockUtil {

    public static CustomerRequestDto mockCustomerRequestDto() {
        CustomerAddressDto addressDto = CustomerAddressDto.builder()
                .addressLine("The Muse 4th Floor")
                .town("Kikuyu")
                .state("Kiambu")
                .country("Kenya")
                .build();

        return CustomerRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .identificationType(IdentificationType.NATIONAL_ID)
                .identificationNumber("39387373")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .addresses(List.of(addressDto))
                .deviceId("device123")
                .deviceType(DeviceOSType.ANDROID)
                .preferredNotificationChannel(NotificationChannel.SMS)
                .build();
    }

    public static ProductRequestDto mockProductRequestDto() {
        ProductFeeDto feeDto = ProductFeeDto.builder()
                .name("Processing Fee")
                .feeType(FeeType.SERVICE_FEE)
                .valueType(LoanFeeValueType.PERCENTAGE)
                .value(BigDecimal.valueOf(1.5))
                .appliedAtOrigination(true)
                .dailyAccrual(false)
                .build();

        return ProductRequestDto.builder()
                .name("Personal Loan")
                .description("A loan for personal use")
                .loanTenure(ProductLoanTenure.FLEXIBLE_TENURE)
                .currency("KES")
                .minLoanAmount(BigDecimal.valueOf(1000))
                .maxLoanAmount(BigDecimal.valueOf(50000))
                .minLoanTermDuration(3)
                .minLoanTermType(LoanTerm.DAY)
                .maxLoanTermDuration(7)
                .maxLoanTermType(LoanTerm.DAY)
                .fees(List.of(feeDto))
                .repaymentFrequency(RepaymentFrequencyType.ONE_OFF)
                .gracePeriodAfterLoanDueDateInDays(10)
                .gracePeriodBeforeLoanDueDateInDays(5)
                .build();
    }

}
