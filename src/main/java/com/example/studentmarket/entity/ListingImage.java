package com.example.studentmarket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "listing_images")
@Getter @Setter @NoArgsConstructor
public class ListingImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_primary")
    private boolean primary;
}
