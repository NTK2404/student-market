package com.example.studentmarket.controller;

import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.repository.CategoryRepository;
import com.example.studentmarket.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ListingRepository listingRepository;

    public HomeController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());

        Pageable top6 = PageRequest.of(0, 6);
        Page<Listing> recentListings = listingRepository.search(null, null, null, null, top6);
        model.addAttribute("recentListings", recentListings.getContent());

        return "index";
    }
}