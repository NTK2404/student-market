package com.example.studentmarket.controller;

import com.example.studentmarket.entity.User;
import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.entity.Report;
import com.example.studentmarket.enums.UserStatus;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.enums.ReportStatus;
import com.example.studentmarket.repository.ListingRepository;
import com.example.studentmarket.repository.ReportRepository;
import com.example.studentmarket.repository.UserRepository;
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
    private ReportRepository reportRepository;

    @Autowired
    private ListingRepository listingRepository;

    // ---------------- BẢNG ĐIỀU KHIỂN ----------------
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }

    // ---------------- QUẢN LÝ NGƯỜI DÙNG ----------------
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    // Khóa tài khoản
    @PostMapping("/users/lock/{id}")
    public String lockUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // Mở khóa tài khoản
    @PostMapping("/users/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }


    // ---------------- QUẢN LÝ BÀI ĐĂNG ----------------
    @GetMapping("/listings")
    public String manageListings(Model model) {
        model.addAttribute("listings", listingRepository.findAll());
        return "admin/listings";
    }

    // Ẩn bài đăng
    @PostMapping("/listings/hide/{id}")
    public String hideListing(@PathVariable Long id) {
        Listing listing = listingRepository.findById(id).orElse(null);
        if (listing != null) {
            listing.setStatus(ListingStatus.HIDDEN);
            listingRepository.save(listing);
        }
        return "redirect:/admin/listings";
    }

    // Hiện bài đăng
    @PostMapping("/listings/show/{id}")
    public String showListing(@PathVariable Long id) {
        Listing listing = listingRepository.findById(id).orElse(null);
        if (listing != null) {
            listing.setStatus(ListingStatus.APPROVED);
            listingRepository.save(listing);
        }
        return "redirect:/admin/listings";
    }

    // Duyệt bài đăng
    @PostMapping("/listings/approve/{id}")
    public String approveListing(@PathVariable Long id) {
        Listing listing = listingRepository.findById(id).orElse(null);
        if (listing != null) {
            listing.setStatus(ListingStatus.APPROVED);
            listingRepository.save(listing);
        }
        return "redirect:/admin/listings";
    }

    // Từ chối bài đăng
    @PostMapping("/listings/reject/{id}")
    public String rejectListing(@PathVariable Long id) {
        Listing listing = listingRepository.findById(id).orElse(null);
        if (listing != null) {
            listing.setStatus(ListingStatus.REJECTED);
            listingRepository.save(listing);
        }
        return "redirect:/admin/listings";
    }

    // Xóa vĩnh viễn bài đăng
    @PostMapping("/listings/delete/{id}")
    public String deleteListing(@PathVariable Long id) {
        if (listingRepository.existsById(id)) {
            listingRepository.deleteById(id);
        }
        return "redirect:/admin/listings";
    }


    // ---------------- QUẢN LÝ BÁO CÁO ----------------
    @GetMapping("/reports")
    public String manageReports(Model model) {
        model.addAttribute("reports", reportRepository.findAll());
        return "admin/reports";
    }

    // Đánh dấu báo cáo đã xử lý
    @PostMapping("/reports/resolve/{id}")
    public String resolveReport(@PathVariable Long id) {
        Report report = reportRepository.findById(id).orElse(null);
        if (report != null) {
            report.setStatus(ReportStatus.RESOLVED);
            reportRepository.save(report);
        }
        return "redirect:/admin/reports";
    }
}