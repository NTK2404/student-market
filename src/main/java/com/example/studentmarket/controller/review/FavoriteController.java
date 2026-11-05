package com.example.studentmarket.controller;

import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.repository.ListingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FavoriteController {

    @Autowired
    private ListingRepository listingRepository;

    // ĐÃ SỬA: Lấy danh sách yêu thích từ Session
    @GetMapping("/favorites")
    public String showFavorites(Model model, HttpSession session) {
        List<Long> favoriteIds = (List<Long>) session.getAttribute("favoriteListings");

        List<Listing> favoriteListings = new ArrayList<>();
        if (favoriteIds != null && !favoriteIds.isEmpty()) {
            favoriteListings = listingRepository.findAllById(favoriteIds);
        }

        model.addAttribute("listings", favoriteListings);
        return "listing/favorites";
    }

    // ĐÃ SỬA: Thêm ID bài đăng vào Session khi bấm nút Yêu thích
    @PostMapping("/favorites/add/{listingId}")
    public String addFavorite(@PathVariable("listingId") Long listingId, HttpSession session) {
        List<Long> favoriteIds = (List<Long>) session.getAttribute("favoriteListings");

        if (favoriteIds == null) {
            favoriteIds = new ArrayList<>();
        }

        // Tránh lưu trùng lặp một bài đăng nhiều lần
        if (!favoriteIds.contains(listingId)) {
            favoriteIds.add(listingId);
            session.setAttribute("favoriteListings", favoriteIds);
        }

        return "redirect:/listings/" + listingId;
    }
}