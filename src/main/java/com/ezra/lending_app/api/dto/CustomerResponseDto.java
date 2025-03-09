package com.ezra.lending_app.api.dto;

import com.ezra.lending_app.domain.enums.DeviceOSType;
import com.ezra.lending_app.domain.enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerResponseDto {
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<CustomerAddressDto> addresses;
    private Instant dateCreated;
    private String deviceId;
    private DeviceOSType deviceType;
    private NotificationChannel preferredNotificationChannel;
}
