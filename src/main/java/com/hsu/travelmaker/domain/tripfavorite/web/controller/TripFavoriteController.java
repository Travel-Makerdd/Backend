package com.hsu.travelmaker.domain.tripfavorite.web.controller;

import com.hsu.travelmaker.domain.tripfavorite.service.TripFavoriteService;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/favorite")
@RequiredArgsConstructor
public class TripFavoriteController {

    private final TripFavoriteService tripFavoriteService;

    // 여행 상품 즐겨찾기 추가
    @PostMapping("/add/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> addTripFavorite(@PathVariable Long tripId) {
        return tripFavoriteService.addTripFavorite(tripId);
    }
    // 즐겨찾기 항목 삭제
    @PostMapping("/remove/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> removeTripFavorite(@PathVariable Long tripId) {
        return tripFavoriteService.removeTripFavorite(tripId);
    }
    // 사용자별 즐겨찾기 목록 조회
    @GetMapping("/checkAll")
    public ResponseEntity<CustomApiResponse<?>> findTripFavoritesByUserId() {
        return tripFavoriteService.findTripFavoritesByUserId();
    }

    
}
