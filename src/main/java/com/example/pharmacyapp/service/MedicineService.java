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

    // Get single medicine (for edit form)
    public Optional<Medicine> getById(Long id) {
        return repository.findById(id);
    }

    // Update existing medicine
    public void updateMedicine(Long id, Medicine updated, Long pharmacyId) {
        repository.findById(id).ifPresent(med -> {
            if (med.getPharmacyId().equals(pharmacyId)) {
                med.setName(updated.getName());
                med.setQuantity(updated.getQuantity());
                med.setImportDate(updated.getImportDate());
                med.setExpiryDate(updated.getExpiryDate());
                repository.save(med);
            }
        });
    }

    // Delete medicine
    public void deleteMedicine(Long id, Long pharmacyId) {
        repository.findById(id).ifPresent(med -> {
            if (med.getPharmacyId().equals(pharmacyId)) {
                repository.delete(med);
            }
        });
    }

    // Sell (minus stock)
    public String sellMedicine(Long id, int qty, Long pharmacyId) {
        Optional<Medicine> opt = repository.findById(id);
        if (opt.isEmpty()) return "Medicine not found.";
        Medicine med = opt.get();
        if (!med.getPharmacyId().equals(pharmacyId)) return "Unauthorized.";
        if (qty <= 0) return "Quantity must be greater than 0.";
        if (med.getQuantity() < qty) return "Not enough stock. Only " + med.getQuantity() + " left.";
        med.setQuantity(med.getQuantity() - qty);
        repository.save(med);
        return null; // null = success
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