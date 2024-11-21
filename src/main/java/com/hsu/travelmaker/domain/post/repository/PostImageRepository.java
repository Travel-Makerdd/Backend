package com.hsu.travelmaker.domain.post.repository;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Post post);
    Optional<PostImage> findFirstByPostId(Post post);
}
