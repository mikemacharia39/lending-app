package com.ezra.lending_app.api.dto.customer;

import java.util.List;

import com.ezra.lending_app.domain.enums.DeviceOSType;
import com.ezra.lending_app.domain.enums.IdentificationType;
import com.ezra.lending_app.domain.enums.NotificationChannel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CustomerRequestDto(
        @NotNull
        @NotBlank
        @Size(min = 1, max = 50)
        String firstName,
        @NotNull
        @NotBlank
        @Size(min = 1, max = 50)
        String lastName,
        @NotNull
        IdentificationType identificationType,
        @NotNull
        @Size(min = 1, max = 30)
        String identificationNumber,
        @Email
        String email,
        @NotNull
        @NotBlank
        @Size(min = 8, max = 13)
        String phoneNumber,
        @NotNull
        @NotEmpty
        List<CustomerAddressDto> addresses,
        String deviceId,
        DeviceOSType deviceType,
        @NotNull
        NotificationChannel preferredNotificationChannel
) {
}
