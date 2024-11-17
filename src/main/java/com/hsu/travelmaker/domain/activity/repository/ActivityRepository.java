package com.hsu.travelmaker.domain.activity.repository;

import com.hsu.travelmaker.domain.schedule.entity.Schedule;
import com.hsu.travelmaker.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findBySchedule(Schedule schedule); // 수정된 메소드
}