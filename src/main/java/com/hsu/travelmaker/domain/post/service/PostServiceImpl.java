package com.hsu.travelmaker.domain.post.service;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.post.entity.PostImage;
import com.hsu.travelmaker.domain.post.repository.PostImageRepository;
import com.hsu.travelmaker.domain.post.repository.PostRepository;
import com.hsu.travelmaker.domain.post.web.dto.*;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createPost(PostCreateDto dto) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();

        // 유효한 사용자 확인
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰이거나, 사용자 정보가 존재하지 않습니다."));
        }

        // 사용자 조회
        User user = userRepository.findById(Long.parseLong(currentUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 게시글 생성
        Post post = Post.builder()
                .user(user)
                .postTitle(dto.getPostTitle())
                .postContent(dto.getPostContent())
                .build();

        postRepository.save(post);

        // 게시글 이미지 생성
        for (String imageUrl : dto.getPostImageUrls()) {
            PostImage postImage = PostImage.builder()
                    .postId(post)
                    .postImageUrl(imageUrl)
                    .build();
            postImageRepository.save(postImage);
        }

        return ResponseEntity.ok(CustomApiResponse.createSuccess(201, null, "게시글이 성공적으로 생성되었습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CustomApiResponse<?>> getPostDetail(Long postId) {
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 해당 게시글에 연결된 이미지 URL 조회
        List<String> postImageUrls = postImageRepository.findByPostId(post)
                .stream()
                .map(PostImage::getPostImageUrl)
                .collect(Collectors.toList());

        // 반환 데이터
        PostResponseDto data = PostResponseDto.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .userId(post.getUser().getUserId())
                .postImageUrls(postImageUrls)
                .build();

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, data, "게시글 조회에 성공했습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CustomApiResponse<?>> getPostAll(int page, int size) {
        // 페이지 요청 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 게시글 목록 조회 (페이지네이션)
        Page<Post> postPage = postRepository.findAll(pageable);

        // 게시글 DTO 리스트 변환
        List<PostListResponseDto> postList = postPage.getContent().stream().map(post -> {
            // 첫 번째 이미지 URL 가져오기
            String firstImageUrl = postImageRepository.findFirstByPostId(post)
                    .map(PostImage::getPostImageUrl)
                    .orElse(null);

            // 게시글 DTO 생성
            return PostListResponseDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContentPreview(post.getPostContent().substring(0, Math.min(post.getPostContent().length(), 50))) // 내용 일부
                    .postImageUrl(firstImageUrl)
                    .build();
        }).collect(Collectors.toList());

        PostListPageResponseDto data = PostListPageResponseDto.builder()
                .posts(postList)
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .currentPage(postPage.getNumber())
                .build();

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, data, "게시글 목록 조회에 성공했습니다."));
    }

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> updatePost(Long postId, PostUpdateDto dto) {
        // 현재 사용자 ID 조회
        String currentUserId = authenticationUserUtils.getCurrentUserId();
        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(CustomApiResponse.createFailWithout(401, "유효하지 않은 토큰입니다."));
        }

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 작성자인지 확인
        if (!post.getUser().getUserId().equals(Long.parseLong(currentUserId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(CustomApiResponse.createFailWithout(403, "게시글을 수정할 권한이 없습니다."));
        }

        // 게시글 내용 수정
        post.setPostTitle(dto.getPostTitle());
        post.setPostContent(dto.getPostContent());
        postRepository.save(post);

        // 기존 이미지 삭제
        postImageRepository.deleteByPostId(post);

        // 새로운 이미지 추가
        for (String imageUrl : dto.getPostImageUrls()) {
            PostImage postImage = PostImage.builder()
                    .postId(post)
                    .postImageUrl(imageUrl)
                    .build();
            postImageRepository.save(postImage);
        }

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "게시글과 이미지가 성공적으로 수정되었습니다."));
    }

}
