package com.hsu.travelmaker.domain.reservation.repository;

import com.hsu.travelmaker.domain.reservation.entity.Reservation;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
