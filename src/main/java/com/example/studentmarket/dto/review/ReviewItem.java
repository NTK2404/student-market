package com.example.studentmarket.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Một đánh giá để hiển thị trên hồ sơ người bán. */
@Getter
@AllArgsConstructor
public class ReviewItem {
    private String reviewerName;
    private String reviewerAvatarUrl;
    private String listingTitle;
    private int rating;
    private String comment;
    private String createdAt; // đã định dạng dd/MM/yyyy
}
