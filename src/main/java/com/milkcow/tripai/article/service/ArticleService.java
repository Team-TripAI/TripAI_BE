package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.exception.ImageException;
import com.milkcow.tripai.image.repository.ImageRepository;
import com.milkcow.tripai.image.result.ImageResult;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ImageRepository imageRepository;

    @Transactional
    public ArticleCreateResponse createArticle(ArticleCreateRequest request, Member member) {

        if (member == null) {
            throw new ArticleException(ArticleResult.NULL_USER_ENTITY);
        }

        Article article = Article.createArticle(request, member);
        Image image = request.toImage();

        Article savedArticle = articleRepository.save(article);
        imageRepository.save(image);

        return ArticleCreateResponse.from(savedArticle.getId());
    }

    public ArticlePageResponse getArticlePage(PageRequest pageRequest) {

        final Page<Article> articlePage = articleRepository.findAll(pageRequest);

        return ArticlePageResponse.from(articlePage);
    }

    public ArticleDetailResponse getArticle(Long id) {

        final Article article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleException(ArticleResult.ARTICLE_NOT_FOUND));

        return ArticleDetailResponse.from(article);
    }

    @Transactional
    public ArticleModifyResponse modifyArticle(Long articleId, ArticleModifyRequest request, Member member) {

        if (member == null) {
            throw new ArticleException(ArticleResult.NULL_USER_ENTITY);
        }

        final Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new ArticleException(ArticleResult.ARTICLE_NOT_FOUND)
        );

        if (!member.equals(article.getMember())) {
            throw new ArticleException(ArticleResult.NOT_ARTICLE_OWNER);
        }

        final Image image = imageRepository.findByImage(request.getImage()).orElseThrow(
                () -> new ImageException(ImageResult.IMAGE_NOT_FOUND)
        );

        article.updateArticle(request.getTitle(), request.getContent(), request.getLocationName(),
                request.getFormattedAddress(), request.getImage());

        image.updateImage(request.getLat(), request.getLng(), request.getLocationName(), request.getFormattedAddress(),
                request.getLabelList(), request.getColorList());

        return ArticleModifyResponse.from(article.getId());
    }
}
