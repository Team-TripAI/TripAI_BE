package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.dto.CommentSearch;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentSearch> search(Long articleId);
}
