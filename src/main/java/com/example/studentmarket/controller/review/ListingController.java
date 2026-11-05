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

    @Autowired
    private ListingImageRepository listingImageRepository;

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

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "listing/create";
    }

    @PostMapping("/create")
    public String saveListing(Listing listing, @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles) {

        User seller = new User();
        seller.setId(1L);
        listing.setSeller(seller);
        listing.setStatus(ListingStatus.PENDING);

        Listing savedListing = listingRepository.save(listing);

        if (imageFiles != null && imageFiles.length > 0) {
            String uploadDir = "uploads/";
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile file : imageFiles) {
                    if (!file.isEmpty()) {
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath);

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

    // ĐÃ SỬA: Lọc riêng bài của user ID 1
    @GetMapping("/my-listings")
    public String myListings(Model model) {
        Pageable pageable = PageRequest.of(0, 20);
        Long currentUserId = 1L; // Gán cứng ID user đăng nhập hiện tại

        Page<Listing> listings = listingRepository.findBySellerId(currentUserId, pageable);
        model.addAttribute("listings", listings);
        return "listing/my-listings";
    }

    // THÊM MỚI: Tính năng xóa
    @GetMapping("/delete/{id}")
    public String deleteListing(@PathVariable("id") Long id) {
        listingRepository.deleteById(id);
        return "redirect:/listings/my-listings";
    }

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