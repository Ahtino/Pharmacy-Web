package com.example.pharmacyapp.repository;
import com.example.pharmacyapp.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    Pharmacy findByName(String name);
}