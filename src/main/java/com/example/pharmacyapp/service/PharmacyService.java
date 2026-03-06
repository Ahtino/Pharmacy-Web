package com.example.pharmacyapp.service;

import com.example.pharmacyapp.model.Pharmacy;
import com.example.pharmacyapp.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository repo;


    public Pharmacy register(Pharmacy pharmacy) {
        pharmacy.hashAndSetPassword(pharmacy.getPassword());
        return repo.save(pharmacy);
    }

    public Pharmacy login(String name, String password) {
        List<Pharmacy> results = repo.findByName(name);
        if (results.isEmpty()) return null;
        Pharmacy p = results.get(0);
        return p.checkPassword(password) ? p : null;
    }

    // Used in controller to check duplicates
    public List<Pharmacy> findByName(String name) {
        return repo.findByName(name);
    }
}