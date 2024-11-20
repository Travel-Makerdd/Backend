package com.hsu.travelmaker.domain.review.web.controller;

import com.hsu.travelmaker.domain.review.service.ReviewService;
import com.hsu.travelmaker.domain.review.web.dto.ReviewCreateDto;
import com.hsu.travelmaker.domain.review.web.dto.ReviewUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    public final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createReview(@RequestBody ReviewCreateDto dto) {
        return reviewService.createReview(dto);
    }
    // 여행상품 리뷰 조회
    @GetMapping("/check/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> getReviewByTrip(@PathVariable Long tripId) {
        return reviewService.getReviewByTrip(tripId);
    }
    // 리뷰 수정
    @PostMapping("/update")
    public ResponseEntity<CustomApiResponse<?>> updateReview(@RequestBody ReviewUpdateDto dto) {
        return reviewService.updateReview(dto);
    }
    // 사용자 리뷰 목록 전체 조회
    @GetMapping("/checkAll")
    public ResponseEntity<CustomApiResponse<?>> getReviewByUser() {
        return reviewService.getReviewByUser();
    }
}
