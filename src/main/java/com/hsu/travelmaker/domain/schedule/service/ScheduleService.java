package com.hsu.travelmaker.domain.Schedule.service;

import com.hsu.travelmaker.global.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;


public interface ScheduleService {
    ResponseEntity<CustomApiResponse<?>> getScheduleByTripId(Long tripId);}
