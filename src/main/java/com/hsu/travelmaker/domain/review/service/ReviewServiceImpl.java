package com.hsu.travelmaker.domain.review.service;

import com.hsu.travelmaker.domain.reservation.repository.ReservationRepository;
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
    private final ReservationRepository reservationRepository;


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

        // 사용자가 해당 여행 상품을 예약했는지 확인
        boolean reservationExists = reservationRepository.findByUserIdAndTripId(user, trip).isPresent();
        if (!reservationExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(403, "해당 여행 상품을 예약한 사용자만 리뷰를 작성할 수 있습니다."));
        }

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
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getReviewByTrip(Long tripId){
        // 여행상품 조회
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행상품을 찾을 수 없습니다."));

        // 여행 상품에 대한 리뷰 조회
        List<Review> reviews = reviewRepository.findByTripId(trip);

        // 리뷰 목록을 DTO로 변환
        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .tripId(review.getTripId().getTripId())
                        .userId(review.getUserId().getUserId())
                        .tripTitle(review.getTripId().getTripTitle())
                        .reviewRating(review.getReviewRating())
                        .reviewContent(review.getReviewContent())
                        .updatedAt(review.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, reviewResponseDtos, "여행 상품 리뷰 조회 성공"));
    }
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> updateReview(ReviewUpdateDto dto) {
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

        // 리뷰 조회
        Review review = reviewRepository.findByUserIdAndTripId(user,
                        tripRepository.findById(dto.getTripId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행상품을 찾을 수 없습니다.")))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 여행 상품에 대한 리뷰를 찾을 수 없습니다."));

        // 리뷰 데이터 업데이트
        if (dto.getReviewRating() != null) {
            review.setReviewRating(dto.getReviewRating());
        }
        if (dto.getReviewContent() != null && !dto.getReviewContent().isEmpty()) {
            review.setReviewContent(dto.getReviewContent());
        }

        // 수정된 리뷰 저장
        reviewRepository.save(review);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "리뷰가 성공적으로 수정되었습니다."));
    }
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getReviewByUser() {
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

        // 사용자의 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findByUserId(user);

        // 리뷰 목록을 DTO 변환
        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .tripId(review.getTripId().getTripId())
                        .userId(review.getUserId().getUserId())
                        .tripTitle(review.getTripId().getTripTitle())
                        .reviewRating(review.getReviewRating())
                        .reviewContent(review.getReviewContent())
                        .updatedAt(review.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, reviewResponseDtos, "사용자의 리뷰 목록 조회 성공"));
    }
}
