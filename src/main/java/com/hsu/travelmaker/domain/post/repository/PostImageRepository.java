package com.hsu.travelmaker.domain.post.repository;

import com.hsu.travelmaker.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
