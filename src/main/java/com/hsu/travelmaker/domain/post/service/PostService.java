package com.hsu.travelmaker.domain.post.service;

import com.hsu.travelmaker.domain.post.web.dto.PostCreateDto;
import com.hsu.travelmaker.domain.post.web.dto.PostUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    ResponseEntity<CustomApiResponse<?>> createPost(String postTitle, String postContent, List<MultipartFile> postImages) throws IOException;
    ResponseEntity<CustomApiResponse<?>> getPostDetail(Long postId);
    ResponseEntity<CustomApiResponse<?>> getPostAll(int page, int size);
    ResponseEntity<CustomApiResponse<?>> updatePost(Long postId, String postTitle, String postContent, List<MultipartFile> postImages) throws IOException;
    ResponseEntity<byte[]> getPostImage(Long postId, String imageName);
}
