package com.example.studentmarket.repository;

import com.example.studentmarket.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findBySellerId(Long sellerId);

    boolean existsByReviewerIdAndListingId(Long reviewerId, Long listingId);

    long countBySellerId(Long sellerId);

    // Điểm trung bình (null nếu chưa có đánh giá nào)
    @Query("select avg(r.rating) from Review r where r.seller.id = :sellerId")
    Double averageRatingBySellerId(@Param("sellerId") Long sellerId);

    // Lấy kèm người đánh giá và tin đăng để hiển thị (tránh lazy-loading ngoài transaction)
    @Query("""
        select r from Review r
        join fetch r.reviewer
        join fetch r.listing
        where r.seller.id = :sellerId
        order by r.createdAt desc
        """)
    List<Review> findBySellerIdWithDetails(@Param("sellerId") Long sellerId);
}
