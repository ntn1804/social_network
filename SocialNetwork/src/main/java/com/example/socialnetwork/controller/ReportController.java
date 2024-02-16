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

    @GetMapping("/create-report")
    public void generateExcelReport(HttpServletResponse response) throws Exception {
        reportService.generateExcel(response);
    }
}
