package com.example.studentmarket.controller;

import com.example.studentmarket.dto.ListingCreateForm;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

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

        // Dùng tài khoản admin@market.local để test theo yêu cầu đề bài
        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";

        listingService.createListing(form, username);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng tin thành công! Tin đang chờ Admin duyệt.");
        return "redirect:/listings/create";
    }

    // Xem trang "Tin của tôi"
    @GetMapping("/my-listings")
    public String showMyListings(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = (userDetails != null) ? userDetails.getUsername() : "admin@market.local";
        List<com.example.studentmarket.entity.Listing> listings = listingService.getListingsBySellerEmail(username);
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
}
