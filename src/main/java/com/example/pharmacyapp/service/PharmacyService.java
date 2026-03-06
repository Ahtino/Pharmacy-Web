package com.example.pharmacyapp.service;

import com.example.pharmacyapp.model.Pharmacy;
import com.example.pharmacyapp.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PharmacyService {
    @Autowired
    private PharmacyRepository repo;

    public Pharmacy register(Pharmacy pharmacy) {
        pharmacy.setPassword(pharmacy.getPasswordHash()); // hash it
        return repo.save(pharmacy);
    }

    public Pharmacy login(String name, String password) {
        Pharmacy p = repo.findByName(name);
        if (p != null && p.checkPassword(password)) {
            return p;
        }
        return null;
    }
}
