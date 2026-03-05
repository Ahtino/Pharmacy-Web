package com.example.pharmacyapp.repository;

import com.example.pharmacyapp.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}