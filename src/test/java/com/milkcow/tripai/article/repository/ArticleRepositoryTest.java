package com.milkcow.tripai.article.repository;


import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.member.domain.Member;
import com.milkcow.tripai.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        final Member member = Member.builder()
                .id(1L)
                .email("abcdef@gmail.com")
                .nickname("닉네임")
                .build();

        final Article article = Article.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .member(member)
                .locationName("장소명")
                .formattedAddress("주소")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .build();

        // when
        memberRepository.save(member);
        final Article result = articleRepository.save(article);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("게시글 제목");
        assertThat(result.getContent()).isEqualTo("내용");
        assertThat(result.getImage()).isEqualTo("36b8f84d-df4e-4d49-b662-bcde71a8764f");
        assertThat(result.getLocationName()).isEqualTo("장소명");
        assertThat(result.getFormattedAddress()).isEqualTo("주소");
        assertThat(result.getMember()).isEqualTo(member);
    }
}