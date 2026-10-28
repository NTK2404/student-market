package com.example.studentmarket.controller;

import com.example.studentmarket.enums.UserStatus;
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
    private com.example.studentmarket.repository.ReportRepository reportRepository;

    // Bổ sung để khắc phục lỗi 404 khi truy cập /admin
    @GetMapping("")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }

    // Hiển thị danh sách người dùng
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    // Chức năng khóa người dùng
    @PostMapping("/users/lock/{id}")
    public String lockUser(@PathVariable Long id) {
        com.example.studentmarket.entity.User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user); // Chỉ dùng userRepository để lưu
        }
        return "redirect:/admin/users";
    }

    // Chức năng mở khóa người dùng
    @PostMapping("/users/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        com.example.studentmarket.entity.User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/reports")
    public String manageReports(Model model) {
        model.addAttribute("reports", reportRepository.findAll());
        return "admin/reports";
    }

    // Chức năng đổi trạng thái báo cáo thành Đã xử lý
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
