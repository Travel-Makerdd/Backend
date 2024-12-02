package com.hsu.travelmaker.domain.trip.service;

import com.hsu.travelmaker.domain.trip.web.dto.TripCreateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface TripService {
    // 여행 상품 등록
    ResponseEntity<CustomApiResponse<?>> createTrip(            String tripTitle,
                                                                String tripDescription,
                                                                BigDecimal tripPrice,
                                                                String startDate,
                                                                String endDate,
                                                                String schedulesJson,
                                                                List<MultipartFile> images);
    // 여행 상품 전체 조회
    ResponseEntity<CustomApiResponse<?>> getAllTrips();
    // 특정 여행 상품 조회
    ResponseEntity<CustomApiResponse<?>> getTripById(Long tripId);

}
