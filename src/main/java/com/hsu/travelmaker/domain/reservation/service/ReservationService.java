package com.hsu.travelmaker.domain.reservation.service;

import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;


public interface ReservationService {
    ResponseEntity<CustomApiResponse<?>> createReservation(Long tripId);
    ResponseEntity<CustomApiResponse<?>> deleteReservation(Long tripId);
    ResponseEntity<CustomApiResponse<?>> getAllReservation();
}
