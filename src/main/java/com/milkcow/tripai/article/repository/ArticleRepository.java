package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
