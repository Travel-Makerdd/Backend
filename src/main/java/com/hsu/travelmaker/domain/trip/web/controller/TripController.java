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


}
