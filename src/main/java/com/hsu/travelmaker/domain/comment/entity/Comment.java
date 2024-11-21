package com.hsu.travelmaker.domain.comment.entity;

import com.hsu.travelmaker.domain.post.entity.Post;
import com.hsu.travelmaker.domain.user.entity.User;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId; // 댓글 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 게시글 FK

    @Lob
    @Column(name = "comment_content")
    private String commentContent; // 댓글 내용

}
