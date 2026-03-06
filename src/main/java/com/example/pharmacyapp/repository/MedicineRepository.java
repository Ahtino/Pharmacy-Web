package com.example.pharmacyapp.repository;

import com.example.pharmacyapp.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByPharmacyId(Long pharmacyId);
}