package com.hsu.travelmaker.domain.postfavorite.web.controller;

import com.hsu.travelmaker.domain.postfavorite.service.PostFavoriteService;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostFavoriteController {

    private final PostFavoriteService postFavoriteService;

    // 게시글 즐겨찾기 추가
    @PostMapping("{postId}/favorite")
    public ResponseEntity<CustomApiResponse<?>> addPostFavorite(@PathVariable Long postId) {
        return postFavoriteService.addPostFavorite(postId);
    }

    // 게시글 즐겨찾기 해제
    @PostMapping("{postId}/unfavorite")
    public ResponseEntity<CustomApiResponse<?>> removePostFavorite(@PathVariable Long postId) {
        return postFavoriteService.removePostFavorite(postId);
    }

    // 게시글 즐겨찾기 조회
    @GetMapping("/getFavorite")
    public ResponseEntity<CustomApiResponse<?>> getPostFavorite() {
        return postFavoriteService.getPostFavorite();
    }

}
