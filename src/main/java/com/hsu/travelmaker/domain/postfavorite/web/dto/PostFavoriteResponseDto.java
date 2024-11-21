package com.hsu.travelmaker.domain.postfavorite.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFavoriteResponseDto {
    private Long postfavoriteId; // 즐겨찾기 ID
    private Long postId;         // 게시글 ID
    private String postTitle;    // 게시글 제목
    private String postContent;  // 게시글 내용 일부
}
