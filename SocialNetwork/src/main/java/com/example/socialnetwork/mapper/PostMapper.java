package com.example.socialnetwork.mapper;

import com.example.socialnetwork.dto.response.ShowAllPostResponseDTO;
import com.example.socialnetwork.entity.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostMapper {
    public List<ShowAllPostResponseDTO> convertPostToShowAllPostResponseDTO(List<Post> posts){
        List<ShowAllPostResponseDTO> responseDTOS = new ArrayList<>();
        posts
                .forEach(post -> responseDTOS.add(ShowAllPostResponseDTO.builder()
                                .id(post.getId())
                                .text(post.getText())
                                .image(post.getImage())
                                .userId(post.getUser().getId())
                        .build()));
        return responseDTOS;
    }
}
