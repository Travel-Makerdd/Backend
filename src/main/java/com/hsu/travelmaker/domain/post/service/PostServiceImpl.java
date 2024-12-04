package com.hsu.travelmaker.domain.post.service;

import com.hsu.travelmaker.domain.comment.repository.CommentRepository;
import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.post.entity.PostImage;
import com.hsu.travelmaker.domain.post.repository.PostImageRepository;
import com.hsu.travelmaker.domain.post.repository.PostRepository;
import com.hsu.travelmaker.domain.post.web.dto.*;
import com.hsu.travelmaker.domain.postfavorite.repository.PostFavoriteRepository;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.domain.user.repository.UserRepository;
import com.hsu.travelmaker.global.response.CustomApiResponse;
import com.hsu.travelmaker.global.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostFavoriteRepository postFavoriteRepository;
    private final AuthenticationUserUtils authenticationUserUtils;

    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createPost(
            String postTitle,
            String postContent,
            List<MultipartFile> postImages
    ) throws IOException {
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
                .postTitle(postTitle)
                .postContent(postContent)
                .build();

        postRepository.save(post);

        // 이미지 파일 저장
        if (postImages != null && !postImages.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/images/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            for (MultipartFile image : postImages) {
                if (!image.isEmpty()) {
                    // 1. 업로드 시간 기반 파일 이름 생성
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
                    String fileExtension = ""; // 확장자 초기화
                    String originalFilename = image.getOriginalFilename();
                    // 2. 파일 확장자 추출
                    if (originalFilename != null && originalFilename.contains(".")) {
                        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }

                    // 3. 최종 파일 이름 생성
                    String newFilename = timeStamp + fileExtension;

                    // 4. 저장 경로 생성
                    String filePath = uploadDir + newFilename;
                    File dest = new File(filePath);
                    image.transferTo(dest);

                    String imageUrl = "http://localhost:8080/images/" + newFilename;

                    // 게시글 이미지 생성
                    PostImage postImage = PostImage.builder()
                            .postId(post)
                            .images(imageUrl)
                            .build();
                    postImageRepository.save(postImage);
                }
            }
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
                .map(postImage -> "/api/post/check/" + post.getPostId() + "/image/" + extractFilename(postImage.getImages()))
                .collect(Collectors.toList());

        // 반환 데이터
        PostResponseDto data = PostResponseDto.builder()
                .postId(post.getPostId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .userId(post.getUser().getUserId())
                .postImageUrls(postImageUrls) // 이미지 URL 포함
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
            // 게시글에 연결된 이미지 URL 목록 조회
            List<String> postImageUrls = postImageRepository.findByPostId(post)
                    .stream()
                    .map(postImage -> "/api/post/check/" + post.getPostId() + "/image/" + extractFilename(postImage.getImages()))
                    .collect(Collectors.toList());

            // 댓글 수와 즐겨찾기 수 조회
            int commentCount = commentRepository.countByPost(post);
            int favoriteCount = postFavoriteRepository.countByPost(post);

            // 게시글 DTO 생성
            return PostListResponseDto.builder()
                    .postId(post.getPostId())
                    .postTitle(post.getPostTitle())
                    .postContentPreview(post.getPostContent().substring(0, Math.min(post.getPostContent().length(), 50))) // 내용 일부
                    .postImageUrls(postImageUrls) // 이미지 URL 목록
                    .commentCount(commentCount)
                    .favoriteCount(favoriteCount)
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
    public ResponseEntity<CustomApiResponse<?>> updatePost(
            Long postId,
            PostUpdateDto postUpdateDto
    ) throws IOException {
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

        // 제목 수정
        postUpdateDto.getPostTitle().ifPresent(post::setPostTitle);

        // 내용 수정
        postUpdateDto.getPostContent().ifPresent(post::setPostContent);

        // 기존 게시글 저장
        postRepository.save(post);

        // 이미지 수정
        if (postUpdateDto.getPostImages().isPresent() && !postUpdateDto.getPostImages().get().isEmpty()) {
            // 기존 이미지 삭제
            postImageRepository.deleteByPostId(post);

            // 새 이미지 파일 저장
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/images/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // 디렉토리 생성
            }

            for (MultipartFile image : postUpdateDto.getPostImages().get()) {
                if (!image.isEmpty()) {
                    // 업로드 시간 기반 파일 이름 생성
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
                    String fileExtension = "";
                    String originalFilename = image.getOriginalFilename();
                    if (originalFilename != null && originalFilename.contains(".")) {
                        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }

                    String newFilename = timeStamp + fileExtension;
                    String filePath = uploadDir + newFilename;
                    File dest = new File(filePath);
                    image.transferTo(dest); // 파일 저장

                    String imageUrl = "http://localhost:8080/images/" + newFilename;

                    PostImage postImage = PostImage.builder()
                            .postId(post)
                            .images(imageUrl)
                            .build();
                    postImageRepository.save(postImage);
                }
            }
        }

        return ResponseEntity.ok(CustomApiResponse.createSuccess(200, null, "게시글과 이미지가 성공적으로 수정되었습니다."));
    }

    @Override
    @Transactional
    public ResponseEntity<byte[]> getPostImage(Long postId, String imageName) {
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

    private String extractFilename(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

}


