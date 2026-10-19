package com.example.studentmarket.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListingCreateForm {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 5, max = 150, message = "Tiêu đề từ 5 đến 150 ký tự")
    private String title;

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 0, message = "Giá bán không được âm")
    private BigDecimal price;

    @NotNull(message = "Vui lòng chọn danh mục")
    private Long categoryId;

    @NotBlank(message = "Vui lòng chọn tình trạng")
    private String condition;

    @NotBlank(message = "Địa điểm không được để trống")
    private String location;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, message = "Mô tả ít nhất 10 ký tự")
    private String description;
}
