package com.hsu.travelmaker.domain.post.web.controller;

import com.hsu.travelmaker.domain.post.service.PostServiceImpl;
import com.hsu.travelmaker.domain.post.web.dto.PostCreateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createPost(@RequestBody PostCreateDto dto) {
        return postService.createPost(dto);
    }

}
