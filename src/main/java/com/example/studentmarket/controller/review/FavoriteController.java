package com.example.studentmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FavoriteController {

    @GetMapping("/favorites")
    public String showFavorites(Model model) {
        return "listing/favorites";
    }
}