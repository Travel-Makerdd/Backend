package com.hsu.travelmaker.domain.review.repository;

import com.hsu.travelmaker.domain.review.entity.Review;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 여행상품과 연관된 리뷰 목록 조회
    List<Review> findByTripId(Trip trip);
    // 사용자와 여행 상품에 해당하는 리뷰 조회
    Optional<Review> findByUserIdAndTripId(User userId, Trip tripId);
    // 사용자와 연관된 리뷰 목록 조회
    List<Review> findByUserId(User user);

}
