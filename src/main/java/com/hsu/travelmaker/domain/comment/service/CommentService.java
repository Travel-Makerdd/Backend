package com.hsu.travelmaker.domain.comment.service;

import com.hsu.travelmaker.domain.comment.web.dto.CommentAddDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<CustomApiResponse<?>> addComment(Long postId, CommentAddDto dto);
    ResponseEntity<CustomApiResponse<?>> getComment(Long postId);
}
