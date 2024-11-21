package com.hsu.travelmaker.domain.reservation.web.controller;

import com.hsu.travelmaker.domain.reservation.service.ReservationService;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    // 예약 생성
    @PostMapping("/create/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> createReservation(@PathVariable Long tripId) {
        return reservationService.createReservation(tripId);
    }
    // 예약 취소
    @PostMapping("/delete/{tripId}")
    public ResponseEntity<CustomApiResponse<?>> deleteReservation(@PathVariable Long tripId) {
        return reservationService.deleteReservation(tripId);
    }
    // 예약 목록 전체 조회
    @GetMapping("/checkAll")
    public ResponseEntity<CustomApiResponse<?>> getAllReservation() {
        return reservationService.getAllReservation();
    }
}
