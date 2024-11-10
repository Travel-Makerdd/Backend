package com.hsu.travelmaker.domain.trip.repository;

import com.hsu.travelmaker.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long>{

}
