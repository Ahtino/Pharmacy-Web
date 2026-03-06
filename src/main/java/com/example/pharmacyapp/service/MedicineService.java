package com.example.pharmacyapp.service;

import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository repository;

    public List<Medicine> getAllMedicines() {
        // Sort by expiryDate ascending (FEFO: earliest expiry first)
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Medicine::getExpiryDate))
                .collect(Collectors.toList());
    }

    public void addMedicine(Medicine medicine) {
        repository.save(medicine);
    }

    public List<Medicine> getExpiringSoon(Long pharmacyId) {
        return getAllMedicines().stream()
                .filter(Medicine::isExpiringSoon)
                .collect(Collectors.toList());
    }

    public List<Medicine> getExpired(Long pharmacyId) {
        return getAllMedicines().stream()
                .filter(Medicine::isExpired)
                .collect(Collectors.toList());
    }

    public List<Medicine> getAllMedicines(Long pharmacyId) {
        return repository.findByPharmacyId(pharmacyId).stream()
                .sorted(Comparator.comparing(Medicine::getExpiryDate))
                .collect(Collectors.toList());
    }

    public void addMedicine(Medicine medicine, Long pharmacyId) {
        medicine.setPharmacyId(pharmacyId);
        repository.save(medicine);
    }

// Update other methods the same way (add pharmacyId parameter)
}
