package com.hsu.travelmaker.domain.postfavorite.repository;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.postfavorite.entity.PostFavorite;
import com.hsu.travelmaker.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<PostFavorite> findByUserAndPost(User user, Post post);
    List<PostFavorite> findByUser(User user);
    int countByPost(Post post);
}
