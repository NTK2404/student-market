package com.example.studentmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FavoriteController {

    @GetMapping("/favorites")
    public String showFavorites(Model model) {
        return "listing/favorites";
    }

    @PostMapping("/favorites/add/{listingId}")
    public String addFavorite(@PathVariable("listingId") Long listingId) {
        System.out.println("Đã thêm bài đăng " + listingId + " vào danh sách yêu thích!");
        return "redirect:/listings/" + listingId;
    }
}