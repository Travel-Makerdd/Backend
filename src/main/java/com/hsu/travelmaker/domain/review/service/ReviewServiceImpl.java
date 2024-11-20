package com.hsu.travelmaker.domain.review.service;

import com.hsu.travelmaker.domain.review.entity.Review;
import com.hsu.travelmaker.domain.review.repository.ReviewRepository;
import com.hsu.travelmaker.domain.review.web.dto.ReviewCreateDto;
import com.hsu.travelmaker.domain.review.web.dto.ReviewResponseDto;
import com.hsu.travelmaker.domain.review.web.dto.ReviewUpdateDto;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final AuthenticationUserUtils authenticationUserUtils;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final TripRepository tripRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createReview(ReviewCreateDto dto) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 여행상품 조회
        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행상품을 찾을 수 없습니다."));

        // 사용자가 해당 여행 상품에 대한 리뷰를 이미 작성했는지 확인
        boolean reviewExists = reviewRepository.findByUserIdAndTripId(user, trip).isPresent();
        if (reviewExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CustomApiResponse.createFailWithout(409, "이미 해당 여행 상품에 대한 리뷰를 작성하셨습니다."));
        }

        // 리뷰 생성 및 저장
        Review review = Review.builder()
                .userId(user)
                .tripId(trip)
                .reviewRating(dto.getReviewRating())
                .reviewContent(dto.getReviewContent())
                .build();
        reviewRepository.save(review);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "리뷰가 성공적으로 생성되었습니다."));
    }

    
}
