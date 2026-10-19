package com.example.studentmarket.service;

import com.example.studentmarket.dto.ListingCreateForm;
import com.example.studentmarket.entity.Listing;
import com.example.studentmarket.entity.User;
import com.example.studentmarket.enums.ListingStatus;
import com.example.studentmarket.repository.ListingRepository;
import com.example.studentmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    public Listing createListing(ListingCreateForm form, String username) {
        User seller = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng: " + username));

        Listing listing = new Listing();
        listing.setTitle(form.getTitle());
        listing.setPrice(form.getPrice());
        listing.setLocation(form.getLocation());
        listing.setDescription(form.getDescription());
        listing.setCondition(form.getCondition());
        
        // Mặc định trạng thái khi vừa đăng là PENDING (chờ duyệt)
        listing.setStatus(ListingStatus.PENDING);
        listing.setViewCount(0);
        listing.setCreatedAt(LocalDateTime.now());
        listing.setUser(seller);

        return listingRepository.save(listing);
    }
}
