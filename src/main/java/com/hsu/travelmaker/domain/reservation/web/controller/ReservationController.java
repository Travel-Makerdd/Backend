package com.hsu.travelmaker.domain.reservation.web.controller;

import com.hsu.travelmaker.domain.reservation.service.ReservationService;
import com.hsu.travelmaker.domain.reservation.web.dto.ReservationCreateDto;
import com.hsu.travelmaker.domain.reservation.web.dto.ReservationDeleteDto;
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
    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<?>> createReservation(@RequestBody ReservationCreateDto dto) {
        return reservationService.createReservation(dto.getTripId());
    }

}
