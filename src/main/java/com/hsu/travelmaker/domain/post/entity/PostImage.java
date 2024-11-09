package com.hsu.travelmaker.domain.post.entity;

import com.hsu.travelmaker.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "POST_IMAGE")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long postImageId; // 게시글 이미지 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId; // 게시글 FK

    @Column(name = "post_image_url", nullable = false)
    private String postImageUrl; // 게시글 제목

}
