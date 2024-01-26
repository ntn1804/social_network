package com.example.socialnetwork.service;

import com.example.socialnetwork.repository.ReportRepository;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;

public interface ReportService {
    long countPostByCreatedDate();

    long countFriendByCreatedDate();

    long countReactByCreatedDate();

    long countCommentByCreatedDate();

    void generateExcel(HttpServletResponse response) throws Exception;
}
