package com.hsu.travelmaker.domain.postfavorite.service;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.post.repository.PostRepository;
import com.hsu.travelmaker.domain.postfavorite.entity.PostFavorite;
import com.hsu.travelmaker.domain.postfavorite.repository.PostFavoriteRepository;
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
public class PostFavoriteServiceImpl implements PostFavoriteService {

    private final PostFavoriteRepository postFavoriteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> addPostFavorite(Long postId) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰입니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 이미 즐겨찾기한 게시글인지 확인
        if (postFavoriteRepository.existsByUserAndPost(user, post)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(400, "이미 즐겨찾기한 게시글입니다."));
        }

        // 즐겨찾기 추가
        PostFavorite postFavorite = PostFavorite.builder()
                .user(user)
                .post(post)
                .build();
        postFavoriteRepository.save(postFavorite);

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "게시글이 즐겨찾기에 추가되었습니다."));
    }

}
