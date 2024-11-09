package com.hsu.travelmaker.domain.post.web.controller;

import com.hsu.travelmaker.domain.post.service.PostServiceImpl;
import com.hsu.travelmaker.domain.post.web.dto.PostCreateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createPost(@RequestBody PostCreateDto dto) {
        return postService.createPost(dto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CustomApiResponse<?>> getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

}
