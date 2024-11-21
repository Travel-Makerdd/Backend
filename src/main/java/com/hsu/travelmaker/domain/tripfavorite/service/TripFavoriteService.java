package com.hsu.travelmaker.domain.tripfavorite.service;

import com.hsu.travelmaker.domain.tripfavorite.entity.TripFavorite;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TripFavoriteService {
    ResponseEntity<CustomApiResponse<?>> addTripFavorite(Long tripId);
    ResponseEntity<CustomApiResponse<?>> removeTripFavorite(Long tripId);
    ResponseEntity<CustomApiResponse<?>> findTripFavoritesByUserId();
}
