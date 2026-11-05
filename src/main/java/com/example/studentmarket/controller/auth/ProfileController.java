package com.example.studentmarket.controller.auth;

import com.example.studentmarket.dto.auth.ChangePasswordForm;
import com.example.studentmarket.dto.auth.ProfileForm;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.service.auth.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // ---------- Hồ sơ ----------

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        // Lấy thông tin user dựa vào email đăng nhập thật
        User u = profileService.getByEmail(auth.getName());

        // ĐÃ SỬA LỖI TRẮNG TRANG: Truyền currentUser xuống View
        model.addAttribute("currentUser", u);

        ProfileForm form = new ProfileForm();
        form.setFullName(u.getFullName());
        form.setPhone(u.getPhone());
        form.setUniversity(u.getUniversity());
        model.addAttribute("profileForm", form);
        return "profile/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @Valid @ModelAttribute("profileForm") ProfileForm form,
                                BindingResult result,
                                @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                Model model,
                                RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "profile/profile";
        }
        try {
            profileService.updateProfile(auth.getName(), form, avatar);
        } catch (IllegalArgumentException e) {
            model.addAttribute("avatarError", e.getMessage());
            return "profile/profile";
        }
        redirect.addFlashAttribute("success", "Đã cập nhật hồ sơ.");
        return "redirect:/profile";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String tooBig() {
        return "redirect:/profile?tooBig";
    }

    // ---------- Đổi mật khẩu ----------

    @GetMapping("/profile/password")
    public String passwordPage(Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "profile/password";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication auth,
                                 @Valid @ModelAttribute("changePasswordForm") ChangePasswordForm form,
                                 BindingResult result,
                                 RedirectAttributes redirect) {
        if (form.getNewPassword() != null && !form.getNewPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Mật khẩu nhập lại không khớp");
        }
        if (!result.hasFieldErrors("currentPassword")
                && !profileService.passwordMatches(auth.getName(), form.getCurrentPassword())) {
            result.rejectValue("currentPassword", "wrong", "Mật khẩu hiện tại không đúng");
        }
        if (!result.hasErrors() && form.getNewPassword().equals(form.getCurrentPassword())) {
            result.rejectValue("newPassword", "same", "Mật khẩu mới phải khác mật khẩu hiện tại");
        }
        if (result.hasErrors()) {
            return "profile/password";
        }
        profileService.changePassword(auth.getName(), form.getNewPassword());
        redirect.addFlashAttribute("success", "Đổi mật khẩu thành công.");
        return "redirect:/profile/password";
    }
}