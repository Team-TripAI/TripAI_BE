package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.repository.CommentRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.repository.ImageRepository;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ImageRepository imageRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public ArticleCreateResponse create(ArticleCreateRequest request, Member member) {

        if (member == null) {
            throw new ArticleException(ArticleResult.NULL_USER_ENTITY);
        }

        Article article = Article.of(request, member);
        Image image = request.toImage();

        Article savedArticle = articleRepository.save(article);
        imageRepository.save(image);

        return ArticleCreateResponse.from(savedArticle.getId());
    }

    public ArticlePageResponse getPage(PageRequest pageRequest) {

        final Page<Article> articlePage = articleRepository.findAll(pageRequest);

        return ArticlePageResponse.from(articlePage);
    }

    public ArticlePageResponse getPage(PageRequest pageRequest, Member member) {

        final Page<Article> articlePage = articleRepository.findAllByMember(pageRequest, member);

        return ArticlePageResponse.from(articlePage);
    }

    public ArticleDetailResponse getDetail(Long id) {

        final Article article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleException(ArticleResult.ARTICLE_NOT_FOUND));

        final List<CommentSearch> searchedComments = commentRepository.search(id);

        return ArticleDetailResponse.of(article, searchedComments);
    }

    @Transactional
    public void remove(Long articleId, Member member) {

        final Article article = articleRepository.findById(articleId).orElseThrow(
            () -> new ArticleException(ArticleResult.ARTICLE_NOT_FOUND)
        );

        checkOwner(member, article);

        commentRepository.deleteAllByArticle(article);

        articleRepository.deleteById(articleId);
    }

    private void checkOwner(Member member, Article article) {
        if (member == null) {
            throw new ArticleException(ArticleResult.NULL_USER_ENTITY);
        }

        if (!member.getId().equals(article.getMember().getId())) {
            throw new ArticleException(ArticleResult.NOT_ARTICLE_OWNER);
        }
    }
}
