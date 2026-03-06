package com.example.pharmacyapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data // Lombok: Generates getters, setters, toString, etc.
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private LocalDate expiryDate;
    private Long pharmacyId;   // ← ADD THIS LINE

    // Helper for Pharmacist: Check if expired
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    // Check if expiring soon (e.g., within 30 days)
    public boolean isExpiringSoon() {
        return !isExpired() && ChronoUnit.DAYS.between(LocalDate.now(), expiryDate) <= 30;
    }
}
