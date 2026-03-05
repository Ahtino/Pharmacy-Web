package com.example.pharmacyapp.controller;

import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MedicineController {
    @Autowired
    private MedicineService service;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("medicines", service.getAllMedicines());
        model.addAttribute("expiring", service.getExpiringSoon());
        model.addAttribute("newMedicine", new Medicine());
        return "index";
    }

    @PostMapping("/add")
    public String addMedicine(@ModelAttribute Medicine medicine) {
        service.addMedicine(medicine);
        return "redirect:/";
    }
}

