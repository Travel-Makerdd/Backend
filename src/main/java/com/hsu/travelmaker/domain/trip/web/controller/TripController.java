package com.hsu.travelmaker.domain.trip.web.controller;

import com.hsu.travelmaker.domain.trip.service.TripServiceImpl;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripServiceImpl tripService;

    // 여행 상품 등록
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createTrip(
            @RequestParam("tripTitle") String tripTitle,
            @RequestParam("tripDescription") String tripDescription,
            @RequestParam("tripPrice") BigDecimal tripPrice,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("schedules") String schedulesJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        // Trip 생성 서비스 호출
        return tripService.createTrip(tripTitle, tripDescription, tripPrice, startDate, endDate, schedulesJson, images);
    }

    // 여행 상품 전체 조회
    @GetMapping("/checkAll")
    public ResponseEntity<CustomApiResponse<?>> getAllTrips() {
        return tripService.getAllTrips();
    }

    // 특정 여행상품 조회
    @GetMapping("/check/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> getTripById(@PathVariable Long tripId) {
        return tripService.getTripById(tripId);
    }

    // 특정 여행상품 이미지 조회
    @GetMapping("/check/{tripId}/image/{imageName}")
    public ResponseEntity<byte[]> getTripImage(
            @PathVariable Long tripId,
            @PathVariable String imageName
    ) {
        return tripService.getTripImage(tripId, imageName);
    }
}
