package com.hsu.travelmaker.domain.post.web.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListPageResponseDto {
    private List<PostListResponseDto> posts;
    private int totalPages;
    private long totalElements;
    private int currentPage;
}
