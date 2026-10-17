package com.example.studentmarket.controller.review;

import com.example.studentmarket.dto.review.ReviewForm;
import com.example.studentmarket.dto.review.ReviewTarget;
import com.example.studentmarket.dto.review.SellerCardInfo;
import com.example.studentmarket.service.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Hồ sơ công khai của người bán và đánh giá người bán. */
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Công khai: ai cũng xem được (đã mở trong SecurityConfig)
    @GetMapping("/users/{id:\\d+}")
    public String sellerProfile(@PathVariable Long id, Model model) {
        SellerCardInfo seller = reviewService.sellerCard(id);
        if (seller == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng");
        }
        model.addAttribute("seller", seller);
        model.addAttribute("reviews", reviewService.reviewsOf(id));
        return "review/seller-profile";
    }

    // Cần đăng nhập
    @GetMapping("/reviews/new")
    public String newReview(@RequestParam Long listingId, Authentication auth, Model model) {
        try {
            ReviewTarget target = reviewService.prepare(auth.getName(), listingId);
            ReviewForm form = new ReviewForm();
            form.setListingId(listingId);
            form.setRating(5);
            model.addAttribute("target", target);
            model.addAttribute("reviewForm", form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("blockedMessage", e.getMessage());
        }
        return "review/review-form";
    }

    @PostMapping("/reviews")
    public String submit(Authentication auth,
                         @Valid @ModelAttribute("reviewForm") ReviewForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        ReviewTarget target;
        try {
            target = reviewService.prepare(auth.getName(), form.getListingId());
        } catch (IllegalArgumentException e) {
            model.addAttribute("blockedMessage", e.getMessage());
            return "review/review-form";
        }
        if (result.hasErrors()) {
            model.addAttribute("target", target);
            return "review/review-form";
        }
        try {
            Long sellerId = reviewService.submit(auth.getName(), form);
            redirect.addFlashAttribute("success", "Cảm ơn bạn đã đánh giá!");
            return "redirect:/users/" + sellerId;
        } catch (IllegalArgumentException e) {
            model.addAttribute("blockedMessage", e.getMessage());
            return "review/review-form";
        }
    }
}
