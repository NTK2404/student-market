package com.example.studentmarket.service.auth;

import com.example.studentmarket.entity.PasswordResetToken;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.enums.UserStatus;
import com.example.studentmarket.repository.PasswordResetTokenRepository;
import com.example.studentmarket.repository.UserRepository;
import com.example.studentmarket.service.mail.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class PasswordResetService {

    private static final int VALID_MINUTES = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Tạo mã và gửi email đặt lại mật khẩu. Nếu email không tồn tại thì không làm gì
     * (và người gọi vẫn báo thông báo giống nhau, để không lộ email nào đã đăng ký).
     */
    @Transactional
    public void requestReset(String email) {
        String normalized = email == null ? "" : email.trim().toLowerCase();
        Optional<User> found = userRepository.findByEmail(normalized);
        if (found.isEmpty() || found.get().getStatus() == UserStatus.LOCKED) {
            return;
        }
        User user = found.get();

        tokenRepository.deleteByExpiresAtBefore(LocalDateTime.now()); // dọn mã hết hạn
        tokenRepository.deleteByUserId(user.getId());                  // mỗi người chỉ có 1 mã còn hiệu lực

        String token = generateToken();
        PasswordResetToken entity = new PasswordResetToken();
        entity.setUser(user);
        entity.setTokenHash(hash(token));
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(VALID_MINUTES));
        tokenRepository.save(entity);

        String link = baseUrl + "/reset-password?token=" + token;
        String body = "Xin chào " + user.getFullName() + ",\n\n"
                + "Bạn (hoặc ai đó) vừa yêu cầu đặt lại mật khẩu cho tài khoản Chợ SV.\n"
                + "Bấm vào liên kết sau để đặt mật khẩu mới (có hiệu lực " + VALID_MINUTES + " phút):\n\n"
                + link + "\n\n"
                + "Nếu bạn không yêu cầu, hãy bỏ qua email này, mật khẩu của bạn không thay đổi.\n";
        emailService.send(user.getEmail(), "Đặt lại mật khẩu - Chợ SV", body);
    }

    @Transactional(readOnly = true)
    public boolean isValid(String token) {
        return findValid(token).isPresent();
    }

    /** @return true nếu đặt lại thành công, false nếu mã sai hoặc hết hạn */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> found = findValid(token);
        if (found.isEmpty()) {
            return false;
        }
        User user = found.get().getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.deleteByUserId(user.getId()); // mã đã dùng, xóa luôn
        return true;
    }

    private Optional<PasswordResetToken> findValid(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return tokenRepository.findByTokenHash(hash(token))
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    private static String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
