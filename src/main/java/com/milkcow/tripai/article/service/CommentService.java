package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.domain.Comment;
import com.milkcow.tripai.article.dto.CommentCreateRequest;
import com.milkcow.tripai.article.dto.CommentCreateResponse;
import com.milkcow.tripai.article.dto.CommentModifyRequest;
import com.milkcow.tripai.article.dto.CommentModifyResponse;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.exception.CommentException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.repository.CommentRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.result.CommentResult;
import com.milkcow.tripai.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    @Transactional
    public CommentCreateResponse createComment(CommentCreateRequest request, Member member) {

        if (member == null) {
            throw new CommentException(CommentResult.NULL_USER_ENTITY);
        }

        Article article = articleRepository.findById(request.getArticleId()).orElseThrow(
                () -> new ArticleException(ArticleResult.ARTICLE_NOT_FOUND)
        );

        // 대댓글 추가 요청인 경우, 부모 댓글이 존재 하는지 확인
        if (request.getCommentId() != null) {
            commentRepository.findById(request.getCommentId()).orElseThrow(
                    () -> new CommentException(CommentResult.COMMENT_NOT_FOUND)
            );
        }

        Comment comment = Comment.createComment(request, article, member);

        Comment savedComment = commentRepository.save(comment);

        return CommentCreateResponse.from(savedComment.getId());
    }

    @Transactional
    public CommentModifyResponse modifyComment(Long commentId, CommentModifyRequest request, Member member) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentException(CommentResult.COMMENT_NOT_FOUND)
        );

        checkOwner(member, comment);

        comment.updateComment(request.getContent());

        return CommentModifyResponse.from(comment.getId());
    }

    private void checkOwner(Member member, Comment comment) {
        if (member == null) {
            throw new ArticleException(CommentResult.NULL_USER_ENTITY);
        }

        if (!member.equals(comment.getMember())) {
            throw new ArticleException(CommentResult.NOT_COMMENT_OWNER);
        }
    }
}
