package com.example.studentmarket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class FavoriteId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "listing_id")
    private Long listingId;
}
