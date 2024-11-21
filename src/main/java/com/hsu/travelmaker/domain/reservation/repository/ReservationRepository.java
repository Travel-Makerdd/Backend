package com.hsu.travelmaker.domain.reservation.repository;

import com.hsu.travelmaker.domain.reservation.entity.Reservation;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 사용자와 연관된 즐겨찾기 목록 조회
    List<Reservation> findByUserId(User user);
    // 사용자와 여행상품과 연관된 예약 조회
    Optional<Reservation> findByUserIdAndTripId(User userId, Trip tripId);
}
