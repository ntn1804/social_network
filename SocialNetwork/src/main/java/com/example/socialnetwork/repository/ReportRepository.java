package com.example.socialnetwork.repository;

import com.example.socialnetwork.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ReportRepository extends JpaRepository<Report, Serializable> {
}
