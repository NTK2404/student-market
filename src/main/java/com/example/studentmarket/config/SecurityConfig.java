package com.example.studentmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                // Khách xem được: trang chủ, danh sách tin, chi tiết tin (id là số),
                // đăng ký, đăng nhập, file tĩnh, ảnh upload.
                // Lưu ý: dùng {id:\\d+} để "/listings/new" KHÔNG bị cho phép với khách.
                .requestMatchers("/", "/listings", "/listings/{id:\\d+}", "/users/{id:\\d+}",
                                 "/register", "/login", "/forgot-password", "/reset-password", "/error",
                                 "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")      // trang đăng nhập riêng (templates/auth/login.html)
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll());
            
        return http.build();
    }
}