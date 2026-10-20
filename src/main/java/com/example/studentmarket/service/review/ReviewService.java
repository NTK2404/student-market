package com.example.studentmarket.service.review;

import com.example.studentmarket.dto.review.ReviewForm;
import com.example.studentmarket.dto.review.ReviewItem;
import com.example.studentmarket.dto.review.ReviewTarget;
import com.example.studentmarket.dto.review.SellerCardInfo;
import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.entity.Review;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.repository.ListingRepository;
import com.example.studentmarket.repository.ReviewRepository;
import com.example.studentmarket.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Đánh giá người bán. Bean tên "reviewService" cũng được gọi trực tiếp từ template
 * (xem templates/review/fragments.html) nên các hàm sellerCard() và stars() là public.
 */
@Service
public class ReviewService {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository,
                         ListingRepository listingRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    // ---------- Hiển thị ----------

    /** Thông tin + điểm đánh giá của người bán; null nếu không tồn tại. */
    @Transactional(readOnly = true)
    public SellerCardInfo sellerCard(Long sellerId) {
        if (sellerId == null) {
            return null;
        }
        return userRepository.findById(sellerId).map(u -> {
            Double avg = reviewRepository.averageRatingBySellerId(u.getId());
            long count = reviewRepository.countBySellerId(u.getId());
            return new SellerCardInfo(u.getId(), u.getFullName(), u.getUniversity(),
                    u.getAvatarUrl(), u.getPhone(), u.getEmail(), avg == null ? 0 : avg, count);
        }).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ReviewItem> reviewsOf(Long sellerId) {
        return reviewRepository.findBySellerIdWithDetails(sellerId).stream()
                .map(r -> new ReviewItem(
                        r.getReviewer().getFullName(),
                        r.getReviewer().getAvatarUrl(),
                        r.getListing().getTitle(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt() == null ? "" : r.getCreatedAt().format(DATE)))
                .toList();
    }

    /** Chuỗi sao, ví dụ stars(4) = "★★★★☆". */
    public String stars(int rating) {
        int r = Math.max(0, Math.min(5, rating));
        return "★".repeat(r) + "☆".repeat(5 - r);
    }

    // ---------- Gửi đánh giá ----------

    /**
     * Kiểm tra người dùng có được đánh giá tin này không và trả về thông tin để hiển thị form.
     * @throws IllegalArgumentException kèm lý do nếu không được đánh giá
     */
    @Transactional(readOnly = true)
    public ReviewTarget prepare(String reviewerEmail, Long listingId) {
        User reviewer = findUser(reviewerEmail);
        Listing listing = checkEligible(reviewer, listingId);
        User seller = listing.getSeller();
        return new ReviewTarget(listing.getId(), listing.getTitle(), seller.getId(), seller.getFullName());
    }

    /** Lưu đánh giá, trả về id người bán để chuyển tới hồ sơ của họ. */
    @Transactional
    public Long submit(String reviewerEmail, ReviewForm form) {
        User reviewer = findUser(reviewerEmail);
        Listing listing = checkEligible(reviewer, form.getListingId());

        Review r = new Review();
        r.setReviewer(reviewer);
        r.setSeller(listing.getSeller());
        r.setListing(listing);
        r.setRating(form.getRating());
        String comment = form.getComment();
        r.setComment(comment == null || comment.isBlank() ? null : comment.trim());
        try {
            reviewRepository.saveAndFlush(r);
        } catch (DataIntegrityViolationException e) { // gửi trùng cùng lúc, vi phạm unique
            throw new IllegalArgumentException("Bạn đã đánh giá tin này rồi");
        }
        return listing.getSeller().getId();
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng: " + email));
    }

    private Listing checkEligible(User reviewer, Long listingId) {
        if (listingId == null) {
            throw new IllegalArgumentException("Thiếu thông tin tin đăng");
        }
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tin đăng"));
        if (listing.getStatus() != ListingStatus.APPROVED && listing.getStatus() != ListingStatus.SOLD) {
            throw new IllegalArgumentException("Tin đăng này chưa thể đánh giá");
        }
        if (listing.getSeller().getId().equals(reviewer.getId())) {
            throw new IllegalArgumentException("Bạn không thể tự đánh giá chính mình");
        }
        if (reviewRepository.existsByReviewerIdAndListingId(reviewer.getId(), listingId)) {
            throw new IllegalArgumentException("Bạn đã đánh giá tin này rồi");
        }
        return listing;
    }
}
