package com.hsu.travelmaker.domain.postfavorite.repository;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.postfavorite.entity.PostFavorite;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long> {
    boolean existsByUserAndPost(User user, Post post);
}
