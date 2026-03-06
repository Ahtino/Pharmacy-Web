package com.example.pharmacyapp.controller;

import com.example.pharmacyapp.model.Medicine;
import com.example.pharmacyapp.model.Pharmacy;
import com.example.pharmacyapp.service.MedicineService;
import com.example.pharmacyapp.service.PharmacyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/")
    public String root() {
        return "redirect:/register";  // Starts at register page
    }

    // ==================== LOGIN PAGE ====================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name,
                        @RequestParam String password,
                        HttpSession session, Model model) {
        Pharmacy pharmacy = pharmacyService.login(name, password);  // Uses raw password
        if (pharmacy != null) {
            session.setAttribute("pharmacyId", pharmacy.getId());
            session.setAttribute("pharmacyName", pharmacy.getName());
            return "redirect:/inventory";
        }
        model.addAttribute("error", "Wrong pharmacy name or password");
        return "login";
    }

    // ==================== REGISTER PAGE ====================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pharmacy", new Pharmacy());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Pharmacy pharmacy, Model model) {
        if (pharmacyService.login(pharmacy.getName(), pharmacy.getPassword()) != null) {  // Check with raw password
            model.addAttribute("error", "Pharmacy name already exists");
            return "register";
        }
        pharmacyService.register(pharmacy);
        return "redirect:/login?registered=true";
    }

    // ==================== INVENTORY (protected) ====================
    @GetMapping("/inventory")
    public String inventory(HttpSession session, Model model) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        if (pharmacyId == null) {
            return "redirect:/login";
        }
        model.addAttribute("medicines", medicineService.getAllMedicines(pharmacyId));
        model.addAttribute("critical", medicineService.getCritical(pharmacyId));        // NEW
        model.addAttribute("expiringSoon", medicineService.getExpiringSoon(pharmacyId));
        model.addAttribute("expired", medicineService.getExpired(pharmacyId));
        model.addAttribute("newMedicine", new Medicine());
        return "inventory";
    }

    @PostMapping("/add")
    public String addMedicine(@ModelAttribute Medicine medicine, HttpSession session) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        medicineService.addMedicine(medicine, pharmacyId);
        return "redirect:/inventory";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
