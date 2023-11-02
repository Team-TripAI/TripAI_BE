package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.QComment;
import com.milkcow.tripai.article.dto.CommentSearch;
import com.milkcow.tripai.article.dto.QCommentSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentSearch> search(Long articleId) {
        QComment qComment = QComment.comment;

        List<CommentSearch> searchedComments = queryFactory
                .select(new QCommentSearch(
                        qComment.id,
                        qComment.parentId.coalesce(qComment.id).as("parentId"),
                        qComment.content,
                        qComment.member.nickname,
                        qComment.createDate,
                        qComment.modifyDate
                ))
                .from(qComment)
                .where(qComment.article.id.eq(articleId))
                .fetch();

        // parentId에 오름차순, commentId에 오름차순으로 정렬
        searchedComments.sort((o1, o2) -> {
            if (o1.getParentId() - o2.getParentId() == 0) {
                return (int) (o1.getCommentId() - o2.getCommentId());
            }
            return (int) (o1.getParentId() - o2.getParentId());
        });

        return searchedComments;
    }
}
