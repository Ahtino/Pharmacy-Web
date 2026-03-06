package com.example.pharmacytracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private LocalDate importDate;   // NEW
    private LocalDate expiryDate;
    private Long pharmacyId;

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon() {
        return !isExpired() && getDaysUntilExpiry() <= 30;
    }

    // NEW: days remaining until expiry
    public long getDaysUntilExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    // NEW: total shelf life in days (import → expiry)
    public long getTotalShelfDays() {
        if (importDate == null) return 0;
        return ChronoUnit.DAYS.between(importDate, expiryDate);
    }
}