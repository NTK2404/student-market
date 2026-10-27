package com.example.studentmarket.controller.auth;

import com.example.studentmarket.dto.auth.ForgotPasswordForm;
import com.example.studentmarket.dto.auth.ResetPasswordForm;
import com.example.studentmarket.service.auth.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Quên mật khẩu: nhập email, nhận link, đặt mật khẩu mới. */
@Controller
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // ---------- Bước 1: nhập email ----------

    @GetMapping("/forgot-password")
    public String forgotPage(Model model) {
        model.addAttribute("forgotForm", new ForgotPasswordForm());
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgot(@Valid @ModelAttribute("forgotForm") ForgotPasswordForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/forgot-password";
        }
        passwordResetService.requestReset(form.getEmail());
        // Luôn báo giống nhau dù email có tồn tại hay không
        return "redirect:/forgot-password?sent";
    }

    // ---------- Bước 2: bấm link trong email, đặt mật khẩu mới ----------

    @GetMapping("/reset-password")
    public String resetPage(@RequestParam(required = false) String token, Model model) {
        if (!passwordResetService.isValid(token)) {
            return "auth/reset-invalid";
        }
        ResetPasswordForm form = new ResetPasswordForm();
        form.setToken(token);
        model.addAttribute("resetForm", form);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String reset(@Valid @ModelAttribute("resetForm") ResetPasswordForm form, BindingResult result) {
        if (!passwordResetService.isValid(form.getToken())) {
            return "auth/reset-invalid";
        }
        if (form.getNewPassword() != null && !form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Mật khẩu nhập lại không khớp");
        }
        if (result.hasErrors()) {
            return "auth/reset-password";
        }
        if (!passwordResetService.resetPassword(form.getToken(), form.getNewPassword())) {
            return "auth/reset-invalid";
        }
        return "redirect:/login?reset";
    }
}
