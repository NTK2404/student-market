package com.example.studentmarket.controller;

import com.example.studentmarket.enums.UserStatus;
import com.example.studentmarket.repository.UserRepository;
import com.example.studentmarket.repository.ListingRepository;
import com.example.studentmarket.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private ReportRepository reportRepository;

    // Khắc phục lỗi 404 khi truy cập /admin
    @GetMapping("")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    // Trang chủ bảng điều khiển Admin
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }

    // --- QUẢN LÝ NGƯỜI DÙNG ---
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/lock/{id}")
    public String lockUser(@PathVariable Long id) {
        com.example.studentmarket.entity.User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        com.example.studentmarket.entity.User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // --- QUẢN LÝ BÀI ĐĂNG (Mới bổ sung) ---
    @GetMapping("/listings")
    public String manageListings(Model model) {
        model.addAttribute("listings", listingRepository.findAll());
        return "admin/listings";
    }

    // --- QUẢN LÝ BÁO CÁO VI PHẠM ---
    @GetMapping("/reports")
    public String manageReports(Model model) {
        model.addAttribute("reports", reportRepository.findAll());
        return "admin/reports";
    }

    @PostMapping("/reports/resolve/{id}")
    public String resolveReport(@PathVariable Long id) {
        com.example.studentmarket.entity.Report report = reportRepository.findById(id).orElse(null);
        if (report != null) {
            report.setStatus(com.example.studentmarket.enums.ReportStatus.RESOLVED);
            reportRepository.save(report);
        }
        return "redirect:/admin/reports";
    }
}
