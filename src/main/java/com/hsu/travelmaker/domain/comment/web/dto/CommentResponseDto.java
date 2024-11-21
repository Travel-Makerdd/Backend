package com.hsu.travelmaker.domain.comment.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;        // 댓글 ID
    private Long userId;           // 사용자 ID
    private Long postId;           // 게시글 ID
    private String userNickname;   // 사용자 이름
    private String commentContent; // 댓글 내용
    private LocalDateTime createdAt; // 작성 날짜
}
