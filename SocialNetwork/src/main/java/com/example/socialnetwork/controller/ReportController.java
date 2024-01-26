package com.example.socialnetwork.controller;

import com.example.socialnetwork.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/count-post")
    public long countPostByCreatedDate(){
        return reportService.countPostByCreatedDate();
    }

    @GetMapping("/count-friend")
    public long getFriendCountByCreatedDate(){
        return reportService.countFriendByCreatedDate();
    }

    @GetMapping("/count-react")
    public long getReactCountByCreatedDate(){
        return reportService.countReactByCreatedDate();
    }

    @GetMapping("/count-comment")
    public long getCommentCountByCreatedDate(){
        return reportService.countCommentByCreatedDate();
    }

    @GetMapping("/create-report")
    public void generateExcelReport(HttpServletResponse response) throws Exception {
        reportService.generateExcel(response);
    }
}
