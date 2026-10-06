package com.example.studentmarket.repository;

import com.example.studentmarket.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findBySellerId(Long sellerId);
    boolean existsByReviewerIdAndListingId(Long reviewerId, Long listingId);
}
