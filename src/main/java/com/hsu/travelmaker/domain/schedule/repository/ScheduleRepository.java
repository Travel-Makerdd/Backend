package com.hsu.travelmaker.domain.Schedule.repository;

import com.hsu.travelmaker.domain.Schedule.entity.Schedule;
import com.hsu.travelmaker.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 여행 상품에 해당하는 일정들을 조회
    List<Schedule> findByTrip(Trip trip);  // Trip 객체를 통해 일정을 찾도록 수정
}
