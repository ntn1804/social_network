package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.repository.PostRepository;
import com.example.socialnetwork.repository.ReportRepository;
import com.example.socialnetwork.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PostRepository postRepository;

    public long getPostCountByCreatedDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return postRepository.countByCreatedDate(cal.getTime());
    }
}
