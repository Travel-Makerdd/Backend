package com.hsu.travelmaker.domain.trip.repository;

import com.hsu.travelmaker.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long>{
    // 여행 상품 전체 조회
    List<Trip> findAll();
    // 여행 상품 상세 조회
    Optional<Trip> findByTripId(Long tripId);  // tripId를 Long 타입으로 변경

}
