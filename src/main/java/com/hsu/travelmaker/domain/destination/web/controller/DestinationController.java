package com.hsu.travelmaker.domain.destination.web.controller;

import com.hsu.travelmaker.domain.destination.service.DestinationService;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/destination")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping("/checkAll")
    public ResponseEntity<CustomApiResponse<?>> getAllDestinations() {
        return destinationService.getAllDestinations();
    }

    @GetMapping("/check/{destinationId}")
    public ResponseEntity<CustomApiResponse<?>> getDestination(@PathVariable Long destinationId) {
        return destinationService.getDestinationById(destinationId);
    }
    @GetMapping("/check/{destinationId}/image/{imageName}")
    public ResponseEntity<byte[]> getDestinationImage(
            @PathVariable Long destinationId,
            @PathVariable String imageName
    ) {
        return destinationService.getDestinationImage(destinationId, imageName);
    }

}

