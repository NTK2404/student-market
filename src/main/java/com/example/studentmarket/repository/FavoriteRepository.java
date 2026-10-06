package com.example.studentmarket.repository;

import com.example.studentmarket.entity.Favorite;
import com.example.studentmarket.entity.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUserId(Long userId);
}
