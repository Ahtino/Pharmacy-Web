package com.example.pharmacyapp.repository;

import com.example.pharmacyapp.model.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT p FROM Pharmacy p WHERE p.name = :name")
    List<Pharmacy> findByName(@Param("name") String name);
}