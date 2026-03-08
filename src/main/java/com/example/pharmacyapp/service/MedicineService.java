package com.example.pharmacyapp.service;

import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository repository;

    public List<Medicine> getAllMedicines(Long pharmacyId) {
        return repository.findByPharmacyId(pharmacyId).stream()
                .sorted(Comparator.comparing(Medicine::getExpiryDate))
                .collect(Collectors.toList());
    }

    public void addMedicine(Medicine medicine, Long pharmacyId) {
        medicine.setPharmacyId(pharmacyId);
        repository.save(medicine);
    }

    // Update quantity directly
    public String updateQuantity(Long id, int newQty, Long pharmacyId) {
        Optional<Medicine> opt = repository.findById(id);
        if (opt.isEmpty()) return "Medicine not found.";
        Medicine med = opt.get();
        if (!med.getPharmacyId().equals(pharmacyId)) return "Unauthorized.";
        if (newQty < 0) return "Quantity cannot be negative.";
        med.setQuantity(newQty);
        repository.save(med);
        return null;
    }

    // Delete medicine
    public void deleteMedicine(Long id, Long pharmacyId) {
        repository.findById(id).ifPresent(med -> {
            if (med.getPharmacyId().equals(pharmacyId)) {
                repository.delete(med);
            }
        });
    }

    public List<Medicine> getCritical(Long pharmacyId) {
        return getAllMedicines(pharmacyId).stream()
                .filter(m -> !m.isExpired() && m.getDaysUntilExpiry() <= 10)
                .collect(Collectors.toList());
    }

    public List<Medicine> getExpiringSoon(Long pharmacyId) {
        return getAllMedicines(pharmacyId).stream()
                .filter(m -> !m.isExpired() && m.getDaysUntilExpiry() > 10 && m.getDaysUntilExpiry() <= 30)
                .collect(Collectors.toList());
    }

    public List<Medicine> getExpired(Long pharmacyId) {
        return getAllMedicines(pharmacyId).stream()
                .filter(Medicine::isExpired)
                .collect(Collectors.toList());
    }
}