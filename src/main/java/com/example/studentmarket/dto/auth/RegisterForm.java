package com.example.studentmarket.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** Dữ liệu form đăng ký (kèm quy tắc kiểm tra). */
@Getter @Setter
public class RegisterForm {

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullName;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    @Size(max = 150, message = "Email tối đa 150 ký tự")
    private String email;

    // Không bắt buộc: để trống hoặc đúng dạng 0xxxxxxxxx / +84xxxxxxxxx
    @Pattern(regexp = "^$|^(0|\\+84)\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Size(max = 150, message = "Tên trường tối đa 150 ký tự")
    private String university;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, max = 50, message = "Mật khẩu từ 6 đến 50 ký tự")
    private String password;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String confirmPassword;
}
