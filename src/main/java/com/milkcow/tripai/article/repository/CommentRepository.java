package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
