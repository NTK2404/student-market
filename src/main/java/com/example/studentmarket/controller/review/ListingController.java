package com.example.studentmarket.controller;

import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.entity.ListingImage;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.repository.CategoryRepository;
import com.example.studentmarket.repository.ListingImageRepository;
import com.example.studentmarket.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // BỔ SUNG: Gọi Repository của bảng ảnh
    @Autowired
    private ListingImageRepository listingImageRepository;

    // 1. Xử lý bộ lọc tìm kiếm ở trang danh sách
    @GetMapping
    public String listListings(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 12);
        Page<Listing> listings = listingRepository.search(keyword, categoryId, minPrice, maxPrice, pageable);

        model.addAttribute("listings", listings);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "listing/list";
    }

    // 2. Hiển thị trang Đăng tin
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "listing/create";
    }

    // 3. ĐÃ SỬA: Xử lý lưu tin đăng mới và upload ảnh
    @PostMapping("/create")
    public String saveListing(Listing listing, @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles) {

        // Gán tạm user có ID = 1 để tránh lỗi bắt buộc khóa ngoại seller_id
        User seller = new User();
        seller.setId(1L);
        listing.setSeller(seller);
        listing.setStatus(ListingStatus.PENDING);

        // Lưu listing trước để hệ thống sinh ra ID bài đăng
        Listing savedListing = listingRepository.save(listing);

        // Logic xử lý lưu ảnh vật lý vào thư mục 'uploads' và ghi vào database
        if (imageFiles != null && imageFiles.length > 0) {
            String uploadDir = "uploads/";
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // Tạo thư mục nếu chưa có
                }

                for (MultipartFile file : imageFiles) {
                    if (!file.isEmpty()) {
                        // Đổi tên file ngẫu nhiên để tránh trùng lặp
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        Path filePath = uploadPath.resolve(fileName);

                        // Copy file ảnh vào thư mục
                        Files.copy(file.getInputStream(), filePath);

                        // Lưu thông tin đường dẫn ảnh vào database bảng listing_images
                        ListingImage listingImage = new ListingImage();
                        listingImage.setListing(savedListing);
                        listingImage.setImageUrl("/uploads/" + fileName);
                        listingImageRepository.save(listingImage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/";
    }

    // 4. Xử lý trang "Tin của tôi"
    @GetMapping("/my-listings")
    public String myListings(Model model) {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Listing> listings = listingRepository.search(null, null, null, null, pageable);

        model.addAttribute("listings", listings);
        return "listing/my-listings";
    }

    // 5. Xử lý xem chi tiết tin đăng
    @GetMapping("/{id}")
    public String viewListingDetail(@PathVariable("id") Long id, Model model) {
        Listing listing = listingRepository.findById(id).orElse(null);

        if (listing == null) {
            return "redirect:/listings";
        }

        model.addAttribute("listing", listing);

        Long catId = (listing.getCategory() != null) ? listing.getCategory().getId() : null;
        Pageable top4 = PageRequest.of(0, 5);
        Page<Listing> relatedPage = listingRepository.search(null, catId, null, null, top4);

        List<Listing> relatedListings = relatedPage.getContent().stream()
                .filter(l -> !l.getId().equals(id))
                .limit(4)
                .collect(Collectors.toList());

        model.addAttribute("relatedListings", relatedListings);

        return "listing/detail";
    }
}