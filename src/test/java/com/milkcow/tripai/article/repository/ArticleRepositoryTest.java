package com.milkcow.tripai.article.repository;


import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 게시글작성() {
        // given
        final Member member = getMember();
        memberRepository.save(member);
        memberRepository.flush();

        final Article article = getArticle(member);

        // when
        final Article result = articleRepository.save(article);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("게시글 제목");
        assertThat(result.getContent()).isEqualTo("게시글 내용");
        assertThat(result.getImage()).isEqualTo("36b8f84d-df4e-4d49-b662-bcde71a8764f");
        assertThat(result.getLocationName()).isEqualTo("장소명");
        assertThat(result.getFormattedAddress()).isEqualTo("주소");
        assertThat(result.getMember()).isEqualTo(member);
    }

    @Test
    public void 게시글목록조회_사이즈가0() {
        // given
        final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());

        // when
        final Page<Article> result = articleRepository.findAll(pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void 게시글목록조회_사이즈가2() {
        // given
        final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());
        final Member member = getMember();
        memberRepository.save(member);
        memberRepository.flush();

        final Article article1 = getArticle(member);
        final Article article2 = getArticle(member);
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.flush();

        // when
        final Page<Article> result = articleRepository.findAll(pageRequest);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void 게시글상세조회() {
        // given
        final Member member = getMember();
        memberRepository.save(member);
        memberRepository.flush();

        final Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .member(member)
                .locationName("장소명")
                .formattedAddress("주소")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .build();

        // when
        Long id = articleRepository.save(article).getId();
        final Article result = articleRepository.findById(id).get();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("게시글 제목");
        assertThat(result.getContent()).isEqualTo("게시글 내용");
        assertThat(result.getImage()).isEqualTo("36b8f84d-df4e-4d49-b662-bcde71a8764f");
        assertThat(result.getLocationName()).isEqualTo("장소명");
        assertThat(result.getFormattedAddress()).isEqualTo("주소");
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getCreateDate()).isNotNull();
        assertThat(result.getModifyDate()).isNotNull();
    }

    @Test
    public void 게시글추가후삭제() {
        // given
        final Member member = getMember();
        final Article article = getArticle(member);
        memberRepository.save(member);

        final Article savedArticle = articleRepository.save(article);

        // when
        articleRepository.deleteById(savedArticle.getId());

        // then
    }

    private Member getMember() {
        return Member.builder()
                .email("abcdef@gmail.com")
                .nickname("닉네임")
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