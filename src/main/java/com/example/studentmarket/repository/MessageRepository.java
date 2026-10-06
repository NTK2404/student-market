package com.example.studentmarket.repository;

import com.example.studentmarket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByListingIdOrderBySentAtAsc(Long listingId);
}
