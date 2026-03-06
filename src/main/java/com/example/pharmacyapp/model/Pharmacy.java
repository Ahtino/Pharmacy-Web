package com.example.pharmacyapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String passwordHash;

    @Transient
    private String password; // form input only, NOT saved to DB

    // Called during registration to hash and store
    public void hashAndSetPassword(String rawPassword) {
        if (rawPassword != null && !rawPassword.isEmpty()) {
            this.passwordHash = new BCryptPasswordEncoder().encode(rawPassword);
        }
    }

    public boolean checkPassword(String rawPassword) {
        if (rawPassword == null || passwordHash == null) return false;
        return new BCryptPasswordEncoder().matches(rawPassword, this.passwordHash);
    }
}