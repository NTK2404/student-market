package com.example.studentmarket.controller;

import com.example.studentmarket.enums.UserStatus;
import com.example.studentmarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Gọi Repository để tương tác với bảng Users trong Database
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard";
    }

    // Chức năng mới: Hiển thị danh sách người dùng
    @GetMapping("/users")
    public String manageUsers(Model model) {
        // Lấy toàn bộ người dùng từ database và truyền sang file HTML
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }
    // Chức năng khóa người dùng
    @org.springframework.web.bind.annotation.PostMapping("/users/lock/{id}")
    public String lockUser(@org.springframework.web.bind.annotation.PathVariable Long id) {
        com.example.studentmarket.entity.User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user);
        }
        return "redirect:/admin/users"; // Khóa xong thì tự động load lại trang danh sách
    }
    // Chức năng mở khóa người dùng
    @org.springframework.web.bind.annotation.PostMapping("/users/unlock/{id}")
    public String unlockUser(@org.springframework.web.bind.annotation.PathVariable Long id) {
        com.example.studentmarket.entity.User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            // Đổi trạng thái trở lại ACTIVE
            user.setStatus(com.example.studentmarket.enums.UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }
    @org.springframework.beans.factory.annotation.Autowired
    private com.example.studentmarket.repository.ReportRepository reportRepository;

    @org.springframework.web.bind.annotation.GetMapping("/reports")
    public String manageReports(org.springframework.ui.Model model) {
        model.addAttribute("reports", reportRepository.findAll());
        return "admin/reports";
    }
    // Chức năng đổi trạng thái báo cáo thành Đã xử lý
    @org.springframework.web.bind.annotation.PostMapping("/reports/resolve/{id}")
    public String resolveReport(@org.springframework.web.bind.annotation.PathVariable Long id) {
        com.example.studentmarket.entity.Report report = reportRepository.findById(id).orElse(null);
        if (report != null) {
            // Đổi trạng thái báo cáo thành RESOLVED (hoặc APPROVED/DONE tùy Enum của nhóm)
            report.setStatus(com.example.studentmarket.enums.ReportStatus.RESOLVED);
            reportRepository.save(report);
        }
        return "redirect:/admin/reports"; // Tải lại trang sau khi xử lý xong
    }
}