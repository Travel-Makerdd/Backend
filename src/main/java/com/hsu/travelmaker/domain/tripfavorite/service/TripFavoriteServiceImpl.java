package com.hsu.travelmaker.domain.tripfavorite.service;

import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.repository.TripRepository;
import com.hsu.travelmaker.domain.tripfavorite.entity.TripFavorite;
import com.hsu.travelmaker.domain.tripfavorite.repository.TripFavoriteRepository;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripFavoriteServiceImpl implements TripFavoriteService {

    private final TripFavoriteRepository tripFavoriteRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> addTripFavorite(Long tripId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));


        // 여행 상품 조회
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행 상품이 존재하지 않습니다."));

        // 즐겨찾기 생성 및 저장
        TripFavorite tripFavorite = TripFavorite.builder()
                .user(user)
                .trip(trip)
                .build();
        tripFavoriteRepository.save(tripFavorite);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "여행 상품이 즐겨찾기에 추가되었습니다."));
    }

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> removeTripFavorite(Long tripFavoriteId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 즐겨찾기 항목 조회
        TripFavorite tripFavorite = tripFavoriteRepository.findById(tripFavoriteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 즐겨찾기 항목이 존재하지 않습니다."));

        // 즐겨찾기 삭제
        tripFavoriteRepository.delete(tripFavorite);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "여행 상품이 즐겨찾기에서 해제되었습니다."));
    }

}
