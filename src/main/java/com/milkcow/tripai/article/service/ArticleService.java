package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.dto.ArticleCreateRequest;
import com.milkcow.tripai.article.dto.ArticleCreateResponse;
import com.milkcow.tripai.article.dto.ArticlePageResponse;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.repository.ImageRepository;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ImageRepository imageRepository;

    public ArticleCreateResponse createArticle(ArticleCreateRequest request, Member member) {

        if (member == null) {
            throw new ArticleException(ArticleResult.NULL_USER_ENTITY);
        }

        Article article = request.toArticle();
        Image image = request.toImage();

        Article savedArticle = articleRepository.save(article);
        imageRepository.save(image);

        return ArticleCreateResponse.from(savedArticle.getId());
    }

    public ArticlePageResponse getArticlePage(PageRequest pageRequest) {

        final Page<Article> foundArticle = articleRepository.findAll(pageRequest);

        return ArticlePageResponse.from(foundArticle);
    }
}
