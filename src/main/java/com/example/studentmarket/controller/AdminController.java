package com.example.studentmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String showDashboard() {
        // Trả về file giao diện dashboard.html nằm trong thư mục templates/admin/
        return "admin/dashboard";
    }
}