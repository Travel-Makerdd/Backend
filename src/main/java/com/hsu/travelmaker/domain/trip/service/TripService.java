package com.hsu.travelmaker.domain.trip.service;

import com.hsu.travelmaker.domain.trip.web.dto.TripCreateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
public interface TripService {
    // 여행 상품 등록
    ResponseEntity<CustomApiResponse<?>> createTrip(TripCreateDto dto);
    // 여행 상품 전체 조회
    ResponseEntity<CustomApiResponse<?>> getAllTrips();
    // 특정 여행 상품 조회
    ResponseEntity<CustomApiResponse<?>> getTripById(Long tripId);

}
