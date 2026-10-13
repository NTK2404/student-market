package com.example.studentmarket.config;

import com.example.studentmarket.entity.User;
import com.example.studentmarket.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Tự động thêm biến "currentUser" vào MỌI trang, để template (header, v.v.)
 * dùng được ${currentUser.fullName}. currentUser = null nếu chưa đăng nhập.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final UserRepository userRepository;

    public GlobalModelAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }
}
