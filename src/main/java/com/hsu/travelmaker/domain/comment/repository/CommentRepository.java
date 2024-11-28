package com.hsu.travelmaker.domain.comment.repository;

import com.hsu.travelmaker.domain.comment.entity.Comment;
import com.hsu.travelmaker.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 게시글의 댓글 조회
    List<Comment> findByPost(Post post);
    int countByPost(Post post);
}
