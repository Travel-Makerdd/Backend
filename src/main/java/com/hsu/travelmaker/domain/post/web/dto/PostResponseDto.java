package com.hsu.travelmaker.domain.post.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long postId;
    private String postTitle;
    private String postContent;
    private Long userId;
    private List<String> postImageUrls;
    private boolean isFavorite;
}
