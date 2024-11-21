package com.hsu.travelmaker.domain.post.repository;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteByUser(User user);
}
