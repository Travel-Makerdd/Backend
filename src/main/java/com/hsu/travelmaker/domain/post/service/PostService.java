package com.hsu.travelmaker.domain.post.service;

import com.hsu.travelmaker.domain.post.web.dto.PostCreateDto;
import com.hsu.travelmaker.domain.post.web.dto.PostUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<CustomApiResponse<?>> createPost(PostCreateDto dto);
    ResponseEntity<CustomApiResponse<?>> getPostDetail(Long postId);
    ResponseEntity<CustomApiResponse<?>> getPostAll(int page, int size);
    ResponseEntity<CustomApiResponse<?>> updatePost(Long postId, PostUpdateDto dto);
}
