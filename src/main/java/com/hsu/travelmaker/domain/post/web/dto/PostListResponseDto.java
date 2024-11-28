package com.hsu.travelmaker.domain.post.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {
    private Long postId;
    private String postTitle;
    private String postContentPreview;
    private String postImageUrl;
    private int commentCount; // 댓글 수
    private int favoriteCount; // 즐겨찾기 수
}
