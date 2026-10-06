package com.example.studentmarket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "favorites")
@Getter @Setter @NoArgsConstructor
public class Favorite {
    @EmbeddedId
    private FavoriteId id;

    @ManyToOne(fetch = FetchType.LAZY) @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @MapsId("listingId")
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
