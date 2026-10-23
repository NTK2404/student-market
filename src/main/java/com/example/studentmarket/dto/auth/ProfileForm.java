package com.example.studentmarket.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/** Dữ liệu form sửa hồ sơ cá nhân. */
@Getter @Setter
public class ProfileForm {

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullName;

    @Pattern(regexp = "^$|^(0|\\+84)\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Size(max = 150, message = "Tên trường tối đa 150 ký tự")
    private String university;
}
