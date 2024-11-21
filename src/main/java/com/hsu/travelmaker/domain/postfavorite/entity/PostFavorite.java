package com.hsu.travelmaker.domain.postfavorite.entity;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
public class PostFavorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postfavorite_id")
    private Long postfavoriteId; // 게시글 즐겨찾기 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 게시글 FK
}
