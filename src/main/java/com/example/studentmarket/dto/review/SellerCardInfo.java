package com.example.studentmarket.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Thông tin người bán để hiển thị (đã lấy sẵn, không cần lazy-loading). */
@Getter
@AllArgsConstructor
public class SellerCardInfo {
    private Long id;
    private String fullName;
    private String university;
    private String avatarUrl;
    private String phone;
    private String email;
    private double average; // điểm trung bình, 0 nếu chưa có đánh giá
    private long count;     // số lượt đánh giá

    /** Điểm làm tròn 0-5 để vẽ sao. */
    public int getRoundedAverage() {
        return (int) Math.round(average);
    }
}
