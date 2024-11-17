package com.hsu.travelmaker.domain.trip.web.controller;

import com.hsu.travelmaker.domain.trip.service.TripService;
import com.hsu.travelmaker.domain.trip.service.TripServiceImpl;
import com.hsu.travelmaker.domain.trip.web.dto.TripCreateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripServiceImpl tripService;

    // 여행 상품 등록
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createTrip(@RequestBody TripCreateDto dto) {
        return tripService.createTrip(dto);
    }

    // 여행 상품 전체 조회
    @GetMapping("/checkAll")
    public ResponseEntity<CustomApiResponse<?>> getAllTrips() {
        return tripService.getAllTrips();
    }

    // 여행 상품 상세 조회
    @GetMapping("/check/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> getTripById(@PathVariable Long tripId) {
        return tripService.getTripById(tripId);
    }

}
