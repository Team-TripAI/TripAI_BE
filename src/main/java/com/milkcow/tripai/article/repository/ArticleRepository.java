package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByMember(PageRequest pageRequest, Member member);
}
