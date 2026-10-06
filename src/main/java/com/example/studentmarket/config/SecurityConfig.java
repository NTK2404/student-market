package com.example.studentmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // mật khẩu luôn lưu dạng băm
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Khách xem được: trang chủ, danh sách tin, đăng ký, file tĩnh, ảnh upload
                .requestMatchers("/", "/listings", "/listings/{id}", "/register",
                                 "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            // Tạm dùng trang login mặc định của Spring; sau này đổi sang .loginPage("/login")
            .formLogin(Customizer.withDefaults())
            .logout(l -> l.logoutSuccessUrl("/"));
        return http.build();
    }
}
