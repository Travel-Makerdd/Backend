package com.hsu.travelmaker.domain.destination.repository;

import com.hsu.travelmaker.domain.destination.entity.Destination;
import com.hsu.travelmaker.domain.destination.entity.DestinationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationImageRepository extends JpaRepository<DestinationImage, Long> {
    List<DestinationImage> findByDestination(Destination destination);

}
