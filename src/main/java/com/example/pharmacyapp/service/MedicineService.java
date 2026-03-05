package com.example.pharmacyapp.service;

import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {
    @Autowired
    private MedicineRepository repository;

    public List<Medicine> getAllMedicines() {
        return repository.findAll();
    }

    public void addMedicine(Medicine medicine) {
        repository.save(medicine);
    }

    public List<Medicine> getExpiringSoon() {
        LocalDate now = LocalDate.now();
        return getAllMedicines().stream()
                .filter(m -> m.getExpirationDate().isBefore(now.plusDays(30)))
                .collect(Collectors.toList());
    }
}
