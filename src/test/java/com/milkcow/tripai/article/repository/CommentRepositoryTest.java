package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void 댓글작성() {
        // given
        final Comment comment = getComment(null);

        // when
        Comment result = commentRepository.save(comment);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getParentId()).isNull();
    }

    @Test
    public void 대댓글작성() {
        // given
        final Comment parent = getComment(null);
        Comment savedComment = commentRepository.save(parent);
        final Comment child = getComment(savedComment.getId());

        // when
        Comment result = commentRepository.save(child);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getParentId()).isEqualTo(savedComment.getId());
    }

    private static Comment getComment(Long parentId) {
        return Comment.builder()
                .content("댓글 내용")
                .parentId(parentId)
                .build();
    }
}