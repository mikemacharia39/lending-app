package com.ezra.lending_app.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private Instant dateCreated = Instant.now();

    @LastModifiedDate
    @Column(name = "date_modified", columnDefinition = "DATETIME")
    private Instant dateModified;
}
