package com.hsu.travelmaker.domain.comment.web.controller;

import com.hsu.travelmaker.domain.comment.service.CommentService;
import com.hsu.travelmaker.domain.comment.web.dto.CommentAddDto;
import com.hsu.travelmaker.domain.post.web.dto.PostCreateDto;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/addComment")
    public ResponseEntity<CustomApiResponse<?>> addComment(@PathVariable Long postId, @RequestBody CommentAddDto dto){
        return commentService.addComment(postId, dto);
    }

}
