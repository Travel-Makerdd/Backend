package com.hsu.travelmaker.domain.destination.repository;

import com.hsu.travelmaker.domain.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    // 여행지 전체 조회
    List<Destination> findAll();
    // 여행지 상세 조회
    Optional<Destination> findByDestinationId(Long Destination);
}
