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

    Page<Listing> findBySellerId(Long sellerId, Pageable pageable);

    @Query("""
        SELECT l FROM Listing l 
        WHERE l.status = com.example.studentmarket.enums.ListingStatus.APPROVED 
        AND (:keyword IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) 
        AND (:categoryId IS NULL OR l.category.id = :categoryId) 
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
