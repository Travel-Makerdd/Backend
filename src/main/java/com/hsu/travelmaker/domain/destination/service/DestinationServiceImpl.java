package com.hsu.travelmaker.domain.destination.service;

import com.hsu.travelmaker.domain.destination.entity.Destination;
import com.hsu.travelmaker.domain.destination.repository.DestinationImageRepository;
import com.hsu.travelmaker.domain.destination.repository.DestinationRepository;
import com.hsu.travelmaker.domain.destination.web.dto.DestinationResponseDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationImageRepository destinationImageRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getAllDestinations() {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 모든 여행지 조회
        List<Destination> destinations = destinationRepository.findAll();

        // 여행지 데이터를 DTO로 변환
        List<DestinationResponseDto> destinationResponseDtos = destinations.stream()
                .map(destination -> {
                    // 이미지 URL 구성
                    List<String> imageUrls = destinationImageRepository.findByDestination(destination)
                            .stream()
                            .map(image -> "/api/destination/check/" + destination.getDestinationId() + "/image/" + extractFilename(image.getDestinationImageUrl()))
                            .collect(Collectors.toList());

                    return DestinationResponseDto.builder()
                            .destinationId(destination.getDestinationId())
                            .destinationName(destination.getDestinationName())
                            .destinationDescription(destination.getDestinationDescription())
                            .destinationLocation(destination.getDestinationLocation())
                            .destinationImageUrl(imageUrls) // 이미지 URL 추가
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, destinationResponseDtos, "여행지 목록 조회 성공"));
    }

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> getDestinationById(Long destinationId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 특정 여행지 조회
        Destination destination = destinationRepository.findByDestinationId(destinationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 여행지를 찾을 수 없습니다."));

        // 이미지 URL 구성
        List<String> imageUrls = destinationImageRepository.findByDestination(destination)
                .stream()
                .map(image -> "/api/destination/check/" + destination.getDestinationId() + "/image/" + extractFilename(image.getDestinationImageUrl()))
                .collect(Collectors.toList());

        DestinationResponseDto destinationResponseDto = DestinationResponseDto.builder()
                .destinationId(destination.getDestinationId())
                .destinationName(destination.getDestinationName())
                .destinationDescription(destination.getDestinationDescription())
                .destinationLocation(destination.getDestinationLocation())
                .destinationImageUrl(imageUrls) // 이미지 URL 추가
                .build();

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, destinationResponseDto, "여행지 상세 조회 성공"));
    }

    private String extractFilename(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
    @Override
    @Transactional
    public ResponseEntity<byte[]> getDestinationImage(Long destinationId, String imageName) {
        try {
            // 파일 경로 생성
            Path filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "images", imageName);

            // 파일이 존재하지 않으면 404 반환
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            // 파일을 읽어서 바이너리 데이터를 반환
            byte[] imageBytes = Files.readAllBytes(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(Files.probeContentType(filePath) != null
                    ? MediaType.parseMediaType(Files.probeContentType(filePath))
                    : MediaType.APPLICATION_OCTET_STREAM); // 파일 유형 설정
            headers.setContentLength(imageBytes.length); // 파일 크기 설정

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}


