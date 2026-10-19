package com.example.studentmarket.service;

import com.example.studentmarket.dto.ListingCreateForm;
import com.example.studentmarket.entity.Category;
import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.entity.ListingImage;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.enums.ItemCondition;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.repository.CategoryRepository;
import com.example.studentmarket.repository.ListingRepository;
import com.example.studentmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final String UPLOAD_DIR = "uploads/";

    // 1. Tạo tin đăng kèm upload nhiều ảnh
    public Listing createListing(ListingCreateForm form, String username) {
        User seller = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng: " + username));

        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục id: " + form.getCategoryId()));

        Listing listing = new Listing();
        listing.setTitle(form.getTitle());
        listing.setPrice(form.getPrice());
        listing.setLocation(form.getLocation());
        listing.setDescription(form.getDescription());
        listing.setItemCondition(ItemCondition.valueOf(form.getCondition()));
        listing.setCategory(category);
        listing.setSeller(seller);
        listing.setStatus(ListingStatus.PENDING);
        listing.setViewCount(0);

        // Xử lý upload danh sách ảnh
        if (form.getImages() != null && !form.getImages().isEmpty()) {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            boolean isFirst = true;
            for (MultipartFile file : form.getImages()) {
                if (!file.isEmpty()) {
                    try {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        Path filePath = Paths.get(UPLOAD_DIR + fileName);
                        Files.write(filePath, file.getBytes());

                        ListingImage img = new ListingImage();
                        img.setImageUrl("/uploads/" + fileName);
                        img.setPrimary(isFirst); // Ảnh đầu tiên làm ảnh chính
                        img.setListing(listing);
                        listing.getImages().add(img);

                        isFirst = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return listingRepository.save(listing);
    }

    // 2. Lấy danh sách tin của riêng 1 user (Trang "Tin của tôi")
    public List<Listing> getListingsBySellerEmail(String email) {
        User seller = userRepository.findByEmail(email).orElse(null);
        if (seller == null) return List.of();
        return listingRepository.findAll().stream()
                .filter(l -> l.getSeller().getId().equals(seller.getId()))
                .toList();
    }

    // 3. Đổi trạng thái tin (Ẩn, Đã bán,...)
    public void updateStatus(Long id, ListingStatus status, String username) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin đăng"));
        
        if (listing.getSeller().getEmail().equals(username)) {
            listing.setStatus(status);
            listingRepository.save(listing);
        }
    }

    // 4. Xóa tin đăng
    public void deleteListing(Long id, String username) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin đăng"));
        if (listing.getSeller().getEmail().equals(username)) {
            listingRepository.delete(listing);
        }
    }
}
