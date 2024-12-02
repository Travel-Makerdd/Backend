package com.hsu.travelmaker.domain.trip.web.dto;

import com.hsu.travelmaker.domain.schedule.web.dto.ScheduleResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCreateDto {
    @NotBlank(message = "여행 상품 제목을 입력해주세요.")
    private String tripTitle;

    @NotBlank(message = "여행 상품 설명을 입력해주세요.")
    private String tripDescription;

    @NotBlank(message = "여행 상품 가격을 입력해주세요.")
    private BigDecimal tripPrice;

    @NotBlank(message = "여행 시작일을 입력해주세요.")
    private LocalDate startDate;

    @NotBlank(message = "여행 종료일을 입력해주세요.")
    private LocalDate endDate;

    @NotEmpty(message = "여행상품 이미지를 추가해주세요.")
    private List<MultipartFile> images;

    @NotBlank(message = "일정을 입력해주세요.")
    private List<ScheduleResponseDto> schedules;
}
