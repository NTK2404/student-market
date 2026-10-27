package com.example.studentmarket.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Gửi email (văn bản thuần). Dùng chung cho mọi tính năng cần gửi thư.
 * Nếu chưa cấu hình spring.mail.host thì KHÔNG gửi thật mà in nội dung thư ra log (tab Output),
 * để cả nhóm chạy thử được mà không cần tài khoản email.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${app.mail.from:no-reply@chosv.local}")
    private String from;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSenderProvider = mailSenderProvider;
    }

    /** Không ném lỗi ra ngoài: gửi thất bại chỉ ghi log. */
    public void send(String to, String subject, String body) {
        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (sender == null) {
            log.warn("[Chưa cấu hình email, không gửi thật] Thư gửi tới {}\nTiêu đề: {}\n{}", to, subject, body);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            sender.send(message);
        } catch (Exception e) {
            log.error("Không gửi được email tới {}: {}", to, e.getMessage());
        }
    }
}
