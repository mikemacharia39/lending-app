package com.ezra.lending_app.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_address")
public class CustomerAddress extends BaseEntity {
    @ManyToOne
    private Customer customer;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;
}
