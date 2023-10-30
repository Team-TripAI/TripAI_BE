package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.domain.Comment;
import com.milkcow.tripai.article.dto.CommentCreateRequest;
import com.milkcow.tripai.article.dto.CommentCreateResponse;
import com.milkcow.tripai.article.dto.CommentModifyRequest;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.exception.CommentException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.repository.CommentRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.result.CommentResult;
import com.milkcow.tripai.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService target;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Test
    public void 댓글작성실패_MEMBER가NULL임() {
        // given
        CommentCreateRequest request = getCreateRequest(null);

        // when
        CommentException result = assertThrows(CommentException.class,
                () -> target.createComment(request, null));

        // then
        assertThat(result.getErrorResult()).isEqualTo(CommentResult.NULL_USER_ENTITY);
    }

    @Test
    public void 댓글작성실패_존재하지않은게시글() {
        // given
        CommentCreateRequest request = getCreateRequest(null);
        doReturn(Optional.empty()).when(articleRepository).findById(anyLong());

        // when
        ArticleException result = assertThrows(ArticleException.class,
                () -> target.createComment(request, Member.builder().build()));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.ARTICLE_NOT_FOUND);
    }

    @Test
    public void 대댓글작성실패_부모댓글이존재하지않음() {
        // given
        CommentCreateRequest request = getCreateRequest(-2L);
        doReturn(Optional.empty()).when(commentRepository).findById(anyLong());
        doReturn(Optional.of(Article.builder().build())).when(articleRepository).findById(anyLong());

        // when
        CommentException result = assertThrows(CommentException.class,
                () -> target.createComment(request, Member.builder().build()));

        // then
        assertThat(result.getErrorResult()).isEqualTo(CommentResult.COMMENT_NOT_FOUND);
    }

    @Test
    public void 댓글작성성공() {
        // given
        CommentCreateRequest request = getCreateRequest(null);
        Member member = Member.builder().build();
        doReturn(getComment(null, member)).when(commentRepository).save(any(Comment.class));
        doReturn(Optional.of(Article.builder().build())).when(articleRepository).findById(anyLong());

        // when
        CommentCreateResponse result = target.createComment(request, member);

        // then
        assertThat(result.getCommentId()).isNotNull();
    }

    @Test
    public void 대댓글작성성공() {
        // given
        Member member = Member.builder().build();
        Long parentId = -2L;

        doReturn(Optional.of(Article.builder().build())).when(articleRepository).findById(anyLong());
        doReturn(Optional.of(Comment.builder().id(parentId).build())).when(commentRepository).findById(parentId);
        doReturn(getComment(parentId, member)).when(commentRepository).save(any(Comment.class));

        CommentCreateRequest request = getCreateRequest(-2L);

        // when
        CommentCreateResponse result = target.createComment(request, member);

        // then
        assertThat(result.getCommentId()).isNotNull();
    }

    @Test
    public void 댓글수정실패_댓글이존재하지않음() {
        // given
        CommentModifyRequest request = CommentModifyRequest.builder().content("새 댓글 내용").build();
        doReturn(Optional.empty()).when(commentRepository).findById(anyLong());

        // when
        CommentException result = assertThrows(CommentException.class,
                () -> target.modifyComment(-1L, request, Member.builder().build()));

        // then
        assertThat(result.getErrorResult()).isEqualTo(CommentResult.COMMENT_NOT_FOUND);
    }

    private static Comment getComment(Long parentId, Member member) {
        return Comment.builder()
                .id(-1L)
                .content("댓글 내용")
                .parentId(parentId)
                .article(Article.builder().build())
                .member(member)
                .build();
    }

    private static CommentCreateRequest getCreateRequest(Long commentId) {
        return CommentCreateRequest.builder()
                .content("댓글 내용")
                .commentId(commentId)
                .articleId(-1L)
                .build();
    }
}