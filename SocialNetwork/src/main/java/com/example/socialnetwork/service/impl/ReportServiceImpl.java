package com.example.socialnetwork.service.impl;

import com.example.socialnetwork.config.UserInfoUserDetails;
import com.example.socialnetwork.entity.*;
import com.example.socialnetwork.repository.*;
import com.example.socialnetwork.service.ReportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private ReactRepository reactRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public long countPostByCreatedDate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        List<Post> recentPosts = new ArrayList<>();

        if (user.isPresent()) {
            List<Post> postList = postRepository.findAllByUserId(user.get().getId());

            for (Post post : postList) {
                if (post.getCreatedDate().isAfter(LocalDateTime.now().minusDays(7))) {
                    recentPosts.add(post);
                }
            }
        }
        return recentPosts.size();
    }

    @Override
    public long countFriendByCreatedDate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        List<Friend> recentFriends = new ArrayList<>();

        if(user.isPresent()) {
            List<Friend> friendList = friendRepository.friendList(user.get().getId());

            for (Friend friend : friendList) {
                if (friend.getCreatedDate().isAfter(LocalDateTime.now().minusDays(7))) {
                    recentFriends.add(friend);
                }
            }
        }
        return recentFriends.size();
    }

    @Override
    public long countReactByCreatedDate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        List<React> recentReacts = new ArrayList<>();

        if (user.isPresent()) {
            List<React> reactList = reactRepository.findAllByUserId(user.get().getId());

            for (React react : reactList) {
                if (react.getCreatedDate().isAfter(LocalDateTime.now().minusDays(7))) {
                    recentReacts.add(react);
                }
            }
        }
            return recentReacts.size();
    }

    @Override
    public long countCommentByCreatedDate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        List<Comment> recentComments = new ArrayList<>();

        if (user.isPresent()) {
            List<Comment> commentList = commentRepository.findAllByUserId(user.get().getId());

            for (Comment comment : commentList) {
                if (comment.getCreatedDate().isAfter(LocalDateTime.now().minusDays(7))) {
                    recentComments.add(comment);
                }
            }
        }
        return recentComments.size();
    }

    public void generateExcel(HttpServletResponse response) throws Exception {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=report.xls";

        response.setHeader(headerKey, headerValue);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Posts");
        row.createCell(1).setCellValue("Friends");
        row.createCell(2).setCellValue("Reacts");
        row.createCell(3).setCellValue("Comments");

        HSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(countPostByCreatedDate());
        dataRow.createCell(1).setCellValue(countFriendByCreatedDate());
        dataRow.createCell(2).setCellValue(countReactByCreatedDate());
        dataRow.createCell(3).setCellValue(countCommentByCreatedDate());

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
