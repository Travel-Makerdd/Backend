package com.hsu.travelmaker.domain.comment.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentAddDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String commentContent;

}
