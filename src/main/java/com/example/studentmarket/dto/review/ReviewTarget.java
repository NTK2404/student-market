package com.example.studentmarket.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Thông tin tin đăng và người bán mà người dùng sắp đánh giá. */
@Getter
@AllArgsConstructor
public class ReviewTarget {
    private Long listingId;
    private String listingTitle;
    private Long sellerId;
    private String sellerName;
}
