package com.hsu.travelmaker.domain.post.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateDto {
    private Optional<String> postTitle = Optional.empty();
    private Optional<String> postContent = Optional.empty();
    private Optional<List<MultipartFile>> postImages = Optional.empty();
}
