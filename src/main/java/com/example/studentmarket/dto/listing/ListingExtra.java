package com.example.studentmarket.dto.listing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/** Thông tin phụ của một tin đăng (ảnh, danh mục, người bán), đã lấy sẵn để template dùng. */
@Getter
@AllArgsConstructor
public class ListingExtra {
    private String primaryImage;     // ảnh đại diện, null nếu tin chưa có ảnh
    private List<String> imageUrls;  // tất cả ảnh, ảnh chính đứng đầu
    private Long categoryId;
    private String categoryName;
    private Long sellerId;
    private String sellerName;
}
