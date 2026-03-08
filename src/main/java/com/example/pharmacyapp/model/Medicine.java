package com.example.pharmacyapp.model;

import jakarta.persistence.*;
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
    private LocalDate importDate;
    private LocalDate expiryDate;
    private Long pharmacyId;

    public boolean isExpired() {
        if (expiryDate == null) return false;
        return expiryDate.isBefore(LocalDate.now());
    }

    public boolean isExpiringSoon() {
        if (expiryDate == null) return false;
        return !isExpired() && getDaysUntilExpiry() <= 30;
    }

    public long getDaysUntilExpiry() {
        if (expiryDate == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    public long getTotalShelfDays() {
        if (importDate == null || expiryDate == null) return 0;
        long days = ChronoUnit.DAYS.between(importDate, expiryDate);
        return days > 0 ? days : 0;
    }
}