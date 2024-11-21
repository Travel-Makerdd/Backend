package com.hsu.travelmaker.domain.post.web.controller;

import com.hsu.travelmaker.domain.post.service.PostServiceImpl;
import com.hsu.travelmaker.domain.post.web.dto.PostCreateDto;
import com.hsu.travelmaker.domain.post.web.dto.PostUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    // 게시글 생성
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createPost(@RequestBody PostCreateDto dto) {
        return postService.createPost(dto);
    }

    //게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<CustomApiResponse<?>> getPostDetail(@PathVariable Long postId) {
        return postService.getPostDetail(postId);
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<CustomApiResponse<?>> getPostAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return postService.getPostAll(page, size);
    }

    @PostMapping("/update/{postId}")
    public ResponseEntity<CustomApiResponse<?>> updatePost(@PathVariable Long postId, @RequestBody PostUpdateDto dto) {
        return postService.updatePost(postId, dto);
    }

}
