package com.hsu.travelmaker.domain.review.service;

import com.hsu.travelmaker.domain.review.web.dto.ReviewCreateDto;
import com.hsu.travelmaker.domain.review.web.dto.ReviewUpdateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto dto);
    ResponseEntity<CustomApiResponse<?>> getReviewByTrip(Long tripId);
    ResponseEntity<CustomApiResponse<?>> updateReview(ReviewUpdateDto dto);
    ResponseEntity<CustomApiResponse<?>> getReviewByUser();
}
