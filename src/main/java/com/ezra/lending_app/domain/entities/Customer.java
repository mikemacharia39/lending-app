package com.ezra.lending_app.domain.entities;

import java.util.List;

import com.ezra.lending_app.domain.enums.DeviceOSType;
import com.ezra.lending_app.domain.enums.NotificationChannel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ezra.lending_app.domain.util.RandomReferenceGenerator.generateReference;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {
    @Column(nullable = false, unique = true)
    private final String code = generateReference();

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAddress> addresses;

    @Column(name = "device_id")
    private String deviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private DeviceOSType deviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_channel")
    private final NotificationChannel preferredNotificationChannel = NotificationChannel.SMS;
}
