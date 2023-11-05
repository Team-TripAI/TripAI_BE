package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    // Spring Data JPA 사용시 각 댓글을 n번 삭제하기 때문에, JPQL 사용
    @Modifying
    @Query("delete from Comment c where c.article = :article")
    void deleteAllByArticle(@Param("article") Article article);
}
