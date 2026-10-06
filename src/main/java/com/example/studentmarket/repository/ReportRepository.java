package com.example.studentmarket.repository;

import com.example.studentmarket.entity.Report;
import com.example.studentmarket.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(ReportStatus status);
}
