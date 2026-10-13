package com.example.studentmarket.controller.auth;

import com.example.studentmarket.dto.auth.RegisterForm;
import com.example.studentmarket.service.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /login do Spring Security xử lý, ở đây chỉ hiển thị trang
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
                           BindingResult result) {
        if (form.getPassword() != null && !form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Mật khẩu nhập lại không khớp");
        }
        if (!result.hasFieldErrors("email") && userService.emailExists(form.getEmail())) {
            result.rejectValue("email", "duplicate", "Email này đã được sử dụng");
        }
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.register(form);
        } catch (DataIntegrityViolationException e) { // hai người đăng ký cùng lúc cùng email
            result.rejectValue("email", "duplicate", "Email này đã được sử dụng");
            return "auth/register";
        }
        return "redirect:/login?registered";
    }
}
