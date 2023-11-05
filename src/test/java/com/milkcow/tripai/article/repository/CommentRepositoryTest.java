package com.milkcow.tripai.article.repository;

import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.domain.Comment;
import com.milkcow.tripai.article.dto.CommentSearch;
import com.milkcow.tripai.global.config.TestQueryDslConfig;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestQueryDslConfig.class)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void 댓글작성() {
        // given
        final Comment comment = getComment(null, null, null);

        // when
        Comment result = commentRepository.save(comment);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getParentId()).isNull();
    }

    @Test
    public void 대댓글작성() {
        // given
        final Comment parent = getComment(null, null, null);
        Comment savedComment = commentRepository.save(parent);
        final Comment child = getComment(savedComment.getId(), null, null);

        // when
        Comment result = commentRepository.save(child);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getParentId()).isEqualTo(savedComment.getId());
    }

    @Test
    public void 댓글목록조회_사이즈가0() {
        // given

        // when
        List<CommentSearch> result = commentRepository.search(-1L);

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void 댓글목록조회_사이즈가5() {
        // given
        Member member = Member.builder().nickname("닉네임").build();
        memberRepository.save(member);

        Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .member(member)
                .locationName("장소명")
                .formattedAddress("주소")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .build();
        Long articleId = articleRepository.save(article).getId();

        Comment comment1 = commentRepository.save(getComment(null, article, member));
        Comment comment2 = commentRepository.save(getComment(null, article, member));
        commentRepository.save(getComment(comment2.getId(), article, member));
        commentRepository.save(getComment(comment2.getId(), article, member));
        commentRepository.save(getComment(comment1.getId(), article, member));

        // when
        List<CommentSearch> results = commentRepository.search(articleId);

        // then
        assertThat(results.size()).isEqualTo(5);
        for (CommentSearch result : results) {
            assertThat(result.getParentId()).isNotNull();
        }
        assertThat(results.stream().map(CommentSearch::getParentId).collect(Collectors.toList())).isSortedAccordingTo(Comparator.naturalOrder());
    }

    private static Comment getComment(Long parentId, Article article, Member member) {
        return Comment.builder()
                .content("댓글 내용")
                .parentId(parentId)
                .article(article)
                .member(member)
                .build();
    }

    private Article getArticle(Member member) {
        return Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .member(member)
                .locationName("장소명")
                .formattedAddress("주소")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .build();
    }
}