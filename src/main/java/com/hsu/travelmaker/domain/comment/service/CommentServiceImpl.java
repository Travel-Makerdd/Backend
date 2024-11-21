package com.hsu.travelmaker.domain.comment.service;

import com.hsu.travelmaker.domain.comment.entity.Comment;
import com.hsu.travelmaker.domain.comment.repository.CommentRepository;
import com.hsu.travelmaker.domain.comment.web.dto.CommentAddDto;
import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.post.repository.PostRepository;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> addComment(Long postId, CommentAddDto dto) {
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰입니다."));
        }
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 댓글 생성
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .commentContent(dto.getCommentContent())
                .build();

        commentRepository.save(comment);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "댓글이 성공적으로 추가되었습니다."));
    }
}
