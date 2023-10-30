package com.milkcow.tripai.article.domain;

import com.milkcow.tripai.article.dto.CommentCreateRequest;
import com.milkcow.tripai.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false)
    private String content;

    @Column
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column
    private LocalDateTime modifyDate;

    // 엔티티 생성 메서드
    public static Comment createComment(CommentCreateRequest request, Article article, Member member) {
        Comment comment = Comment.builder()
                .content(request.getContent())
                .member(member)
                .parentId(request.getCommentId())
                .build();

        comment.setArticle(article);

        return comment;
    }

    // 연관관계 메서드
    private void setArticle(Article article) {
        this.article = article;
        article.getCommentList().add(this);
    }
}
