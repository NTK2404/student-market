package com.example.studentmarket.controller;

import com.example.studentmarket.dto.ListingCreateForm;
import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.repository.CategoryRepository;
import com.example.studentmarket.service.auth.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("listingForm")) {
            model.addAttribute("listingForm", new ListingCreateForm());
        }
        return "listing/create";
    }

    @PostMapping("/create")
    public String processCreateListing(
            @Valid @ModelAttribute("listingForm") ListingCreateForm form,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "listing/create";
        }

        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";

        listingService.createListing(form, username);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng tin thành công! Tin đang chờ Admin duyệt.");
        return "redirect:/listings/create";
    }

    // Xem trang "Tin của tôi"
    @GetMapping("/my-listings")
    public String showMyListings(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";
        List<Listing> listings = listingService.getListingsBySellerEmail(username);
        model.addAttribute("listings", listings);
        return "listing/my-listings";
    }

    // Đổi trạng thái tin (Ẩn / Đã bán)
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam ListingStatus status, @AuthenticationPrincipal UserDetails userDetails) {
        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";
        listingService.updateStatus(id, status, username);
        return "redirect:/listings/my-listings";
    }

    // Xóa tin
    @PostMapping("/{id}/delete")
    public String deleteListing(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";
        listingService.deleteListing(id, username);
        return "redirect:/listings/my-listings";
    }

    // Danh sách tìm kiếm, lọc và phân trang (Truy cập qua đường dẫn: /listings)
    @GetMapping("")
    public String searchListings(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) Long categoryId,
                                 @RequestParam(required = false) BigDecimal minPrice,
                                 @RequestParam(required = false) BigDecimal maxPrice,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "9") int size,
                                 Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Listing> listingPage = listingService.searchListings(keyword, categoryId, minPrice, maxPrice, pageable);
        
        model.addAttribute("listingPage", listingPage);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        return "listing/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        if (!model.containsAttribute("listingForm")) {
            model.addAttribute("listingForm", new ListingCreateForm());
        }
        return "listing/create";
    }

    @PostMapping("/new")
    public String processNewListing(
            @Valid @ModelAttribute("listingForm") ListingCreateForm form,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "listing/create";
        }

        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";
        listingService.createListing(form, username);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng tin thành công! Tin đang chờ Admin duyệt.");
        return "redirect:/listings/new";
    }

    // Chi tiết tin đăng (Truy cập qua đường dẫn: /listings/{id})
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Listing listing = listingService.getListingDetail(id);
        List<Listing> relatedListings = listingService.getRelatedListings(listing.getCategory().getId(), id);
        
        model.addAttribute("listing", listing);
        model.addAttribute("relatedListings", relatedListings);
        
        return "listing/detail";
    }
}
