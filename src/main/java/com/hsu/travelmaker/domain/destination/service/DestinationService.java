package com.hsu.travelmaker.domain.destination.service;

import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface DestinationService {
    // 여행지 전체 조회
    ResponseEntity<CustomApiResponse<?>> getAllDestinations();
    // 특정 여행지 조회
    ResponseEntity<CustomApiResponse<?>> getDestinationById(Long DestinationId);
    // 여행지 이미지 조회
    ResponseEntity<byte[]> getDestinationImage(Long destinationId, String imageName);
}
