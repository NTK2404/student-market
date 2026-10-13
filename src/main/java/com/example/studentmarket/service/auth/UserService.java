package com.example.studentmarket.service.auth;

import com.example.studentmarket.dto.auth.RegisterForm;
import com.example.studentmarket.entity.Role;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.repository.RoleRepository;
import com.example.studentmarket.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(normalize(email));
    }

    /** Tạo tài khoản mới với quyền USER, mật khẩu được băm BCrypt. */
    @Transactional
    public User register(RegisterForm form) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Chưa có role USER trong database"));

        User u = new User();
        u.setEmail(normalize(form.getEmail()));
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        u.setFullName(form.getFullName().trim());
        u.setPhone(blankToNull(form.getPhone()));
        u.setUniversity(blankToNull(form.getUniversity()));
        u.setRole(userRole);
        return userRepository.save(u);
    }

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
