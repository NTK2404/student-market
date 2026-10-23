package com.example.studentmarket.service.auth;

import com.example.studentmarket.dto.auth.ProfileForm;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.repository.UserRepository;
import com.example.studentmarket.service.storage.FileStorageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng: " + email));
    }

    /**
     * Cập nhật họ tên, số điện thoại, trường và (nếu có) ảnh đại diện.
     * @throws IllegalArgumentException nếu ảnh không hợp lệ (khi đó không có gì được lưu)
     */
    @Transactional
    public void updateProfile(String email, ProfileForm form, MultipartFile avatar) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng: " + email));

        u.setFullName(form.getFullName().trim());
        u.setPhone(blankToNull(form.getPhone()));
        u.setUniversity(blankToNull(form.getUniversity()));

        String oldAvatar = null;
        if (avatar != null && !avatar.isEmpty()) {
            oldAvatar = u.getAvatarUrl();
            u.setAvatarUrl(fileStorageService.storeImage(avatar, "avatars"));
        }
        userRepository.save(u);

        if (oldAvatar != null) {
            fileStorageService.delete(oldAvatar); // xóa ảnh cũ cho đỡ rác
        }
    }

    @Transactional(readOnly = true)
    public boolean passwordMatches(String email, String rawPassword) {
        return passwordEncoder.matches(rawPassword, getByEmail(email).getPasswordHash());
    }

    @Transactional
    public void changePassword(String email, String newPassword) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng: " + email));
        u.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
