package com.example.pharmacyapp.controller;

import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.model.Pharmacy;
import com.example.pharmacyapp.service.MedicineService;
import com.example.pharmacyapp.service.PharmacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class PharmacyController {

    @Autowired private PharmacyService pharmacyService;
    @Autowired private MedicineService medicineService;

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("error", null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password,
                        HttpSession session, Model model) {
        Pharmacy pharmacy = pharmacyService.login(name, password);
        if (pharmacy != null) {
            session.setAttribute("pharmacyId", pharmacy.getId());
            session.setAttribute("pharmacyName", pharmacy.getName());
            return "redirect:/inventory";
        }
        model.addAttribute("error", "Wrong pharmacy name or password");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pharmacy", new Pharmacy());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Pharmacy pharmacy, Model model) {
        if (!pharmacyService.findByName(pharmacy.getName()).isEmpty()) {
            model.addAttribute("error", "Pharmacy name already exists");
            model.addAttribute("pharmacy", pharmacy);
            return "register";
        }
        pharmacyService.register(pharmacy);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/inventory")
    public String inventory(HttpSession session, Model model,
                            @RequestParam(required = false) String error,
                            @RequestParam(required = false) String success) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        if (pharmacyId == null) return "redirect:/login";
        model.addAttribute("medicines", medicineService.getAllMedicines(pharmacyId));
        model.addAttribute("critical", medicineService.getCritical(pharmacyId));
        model.addAttribute("expiringSoon", medicineService.getExpiringSoon(pharmacyId));
        model.addAttribute("newMedicine", new Medicine());
        model.addAttribute("pharmacyName", session.getAttribute("pharmacyName"));
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "inventory";
    }

    @PostMapping("/add")
    public String addMedicine(@ModelAttribute Medicine medicine,
                              HttpSession session,
                              RedirectAttributes ra) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        if (pharmacyId == null) return "redirect:/login";

        // ── Validation ──

        // 1. Quantity must be 0 or more
        if (medicine.getQuantity() < 0) {
            ra.addAttribute("error", "Quantity cannot be negative.");
            return "redirect:/inventory";
        }

        // 2. Import date cannot be in the future
        if (medicine.getImportDate() != null && medicine.getImportDate().isAfter(LocalDate.now())) {
            ra.addAttribute("error", "Import date cannot be a future date.");
            return "redirect:/inventory";
        }

        // 3. Expiry date must be after import date
        if (medicine.getImportDate() != null && medicine.getExpiryDate() != null
                && !medicine.getExpiryDate().isAfter(medicine.getImportDate())) {
            ra.addAttribute("error", "Expiry date must be after the import date.");
            return "redirect:/inventory";
        }

        medicineService.addMedicine(medicine, pharmacyId);
        ra.addAttribute("success", "Medicine added successfully.");
        return "redirect:/inventory";
    }

//    // Update quantity
//    @PostMapping("/quantity/{id}")
//    public String updateQuantity(@PathVariable Long id,
//                                 @RequestParam int quantity,
//                                 HttpSession session,
//                                 RedirectAttributes ra) {
//        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
//        if (pharmacyId == null) return "redirect:/login";
//
//        if (quantity < 0) {
//            ra.addAttribute("error", "Quantity cannot be negative.");
//            return "redirect:/inventory";
//        }
//
//        String err = medicineService.updateQuantity(id, quantity, pharmacyId);
//        if (err != null) ra.addAttribute("error", err);
//        else ra.addAttribute("success", "Stock updated.");
//        return "redirect:/inventory";
//    }
//
//    // Delete
//    @PostMapping("/delete/{id}")
//    public String delete(@PathVariable Long id,
//                         HttpSession session,
//                         RedirectAttributes ra) {
//        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
//        if (pharmacyId == null) return "redirect:/login";
//        medicineService.deleteMedicine(id, pharmacyId);
//        ra.addAttribute("success", "Medicine deleted.");
//        return "redirect:/inventory";
//    }
}