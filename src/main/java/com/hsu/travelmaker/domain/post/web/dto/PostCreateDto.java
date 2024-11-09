package com.hsu.travelmaker.domain.post.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateDto {

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    private String postTitle;

    @NotBlank(message = "게시글 내용을 입력해주세요.")
    private String postContent;

    @NotEmpty(message = "게시글 이미지를 추가해주세요.")
    private List<String> postImageUrls;

}
