package com.example.studentmarket.service.listing;

import com.example.studentmarket.dto.listing.ListingExtra;
import com.example.studentmarket.entity.ListingImage;
import com.example.studentmarket.enums.ItemCondition;
import com.example.studentmarket.repository.ListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Hàm hỗ trợ hiển thị tin đăng, gọi trực tiếp từ template: ${@listingDisplay.info(listing.id)}.
 * Lấy ảnh, danh mục, người bán TRONG transaction nên không bị LazyInitializationException,
 * bất kể ListingService có JOIN FETCH hay không.
 */
@Service("listingDisplay")
public class ListingDisplayService {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ListingRepository listingRepository;

    public ListingDisplayService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    /** Ảnh, danh mục, người bán của tin; null nếu không tìm thấy tin. */
    @Transactional(readOnly = true)
    public ListingExtra info(Long listingId) {
        if (listingId == null) {
            return null;
        }
        return listingRepository.findById(listingId).map(l -> {
            List<String> urls = l.getImages().stream()
                    .sorted(Comparator.comparing(ListingImage::isPrimary, Comparator.reverseOrder()))
                    .map(ListingImage::getImageUrl)
                    .toList();
            return new ListingExtra(
                    urls.isEmpty() ? null : urls.get(0),
                    urls,
                    l.getCategory().getId(),
                    l.getCategory().getName(),
                    l.getSeller().getId(),
                    l.getSeller().getFullName());
        }).orElse(null);
    }

    public String conditionLabel(ItemCondition condition) {
        if (condition == null) {
            return "";
        }
        return switch (condition) {
            case NEW -> "Mới";
            case LIKE_NEW -> "Như mới";
            case USED -> "Đã sử dụng";
        };
    }

    public String date(LocalDateTime time) {
        return time == null ? "" : time.format(DATE);
    }
}
