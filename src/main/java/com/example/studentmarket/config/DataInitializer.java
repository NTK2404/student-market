package com.example.studentmarket.config;

import com.example.studentmarket.entity.Category;
import com.example.studentmarket.entity.Role;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.repository.CategoryRepository;
import com.example.studentmarket.repository.RoleRepository;
import com.example.studentmarket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/** Tạo dữ liệu khởi tạo (chỉ thêm khi chưa có). */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seed(RoleRepository roles, UserRepository users,
                           CategoryRepository categories, PasswordEncoder encoder) {
        return args -> {
            if (roles.count() == 0) {
                roles.save(new Role("USER"));
                roles.save(new Role("ADMIN"));
            }
            if (!users.existsByEmail("admin@market.local")) {
                User admin = new User();
                admin.setEmail("admin@market.local");
                admin.setPasswordHash(encoder.encode("admin123")); // ĐỔI mật khẩu này khi nộp bài
                admin.setFullName("Quản trị viên");
                admin.setRole(roles.findByName("ADMIN").orElseThrow());
                users.save(admin);
            }
            if (categories.count() == 0) {
                for (String name : List.of("Sách & giáo trình", "Đồ điện tử", "Quần áo",
                                           "Đồ dùng học tập", "Xe cộ", "Khác")) {
                    Category c = new Category();
                    c.setName(name);
                    categories.save(c);
                }
            }
        };
    }
}
