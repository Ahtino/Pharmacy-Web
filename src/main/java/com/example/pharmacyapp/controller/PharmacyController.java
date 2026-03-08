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
    public String addMedicine(@ModelAttribute Medicine medicine, HttpSession session) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        if (pharmacyId == null) return "redirect:/login";
        medicineService.addMedicine(medicine, pharmacyId);
        return "redirect:/inventory";
    }

    // Update quantity
    @PostMapping("/quantity/{id}")
    public String updateQuantity(@PathVariable Long id,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        if (pharmacyId == null) return "redirect:/login";
        String err = medicineService.updateQuantity(id, quantity, pharmacyId);
        if (err != null) return "redirect:/inventory?error=" + err.replace(" ", "+");
        return "redirect:/inventory?success=Quantity+updated";
    }

    // Delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        Long pharmacyId = (Long) session.getAttribute("pharmacyId");
        if (pharmacyId == null) return "redirect:/login";
        medicineService.deleteMedicine(id, pharmacyId);
        return "redirect:/inventory?success=Medicine+deleted";
    }
}