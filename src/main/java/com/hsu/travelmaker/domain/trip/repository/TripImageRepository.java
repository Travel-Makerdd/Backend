package com.hsu.travelmaker.domain.trip.repository;

import com.hsu.travelmaker.domain.trip.entity.Trip;
import com.hsu.travelmaker.domain.trip.entity.TripImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripImageRepository extends JpaRepository<TripImage, Long>{
    List<TripImage> findByTripId(Trip Trip);
}
