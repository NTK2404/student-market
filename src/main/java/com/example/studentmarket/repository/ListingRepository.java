package com.example.studentmarket.repository;

import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.enums.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    Page<Listing> findByStatus(ListingStatus status, Pageable pageable);

    // Đã có sẵn hàm này để phục vụ trang "Tin của tôi"
    Page<Listing> findBySellerId(Long sellerId, Pageable pageable);

    // Dùng LEFT JOIN để giữ bài đăng bị lỗi danh mục, và xử lý categoryId = 0 an toàn
    @Query("""
        SELECT l FROM Listing l 
        LEFT JOIN l.category c
        WHERE l.status = com.example.studentmarket.enums.ListingStatus.APPROVED 
        AND (:keyword IS NULL OR :keyword = '' OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) 
        AND (:categoryId IS NULL OR :categoryId = 0 OR c.id = :categoryId) 
        AND (:minPrice IS NULL OR l.price >= :minPrice) 
        AND (:maxPrice IS NULL OR l.price <= :maxPrice)
    """)
    Page<Listing> search(@Param("keyword") String keyword,
                         @Param("categoryId") Long categoryId,
                         @Param("minPrice") BigDecimal minPrice,
                         @Param("maxPrice") BigDecimal maxPrice,
                         Pageable pageable);

    List<Listing> findByCategoryIdAndIdNotAndStatus(Long categoryId, Long id, ListingStatus status);
}