package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.dto.ArticleCreateRequest;
import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.dto.ArticleCreateResponse;
import com.milkcow.tripai.article.dto.ArticlePageResponse;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.repository.ImageRepository;
import com.milkcow.tripai.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @InjectMocks
    private ArticleService target;
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ImageRepository imageRepository;

    @Test
    public void 게시글작성실패_MEMBER가NULL임() {
        // given
        ArticleCreateRequest request = getArticleCreateRequest();

        // when
        final ArticleException result = assertThrows(ArticleException.class,
                () -> target.createArticle(request, null));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.NULL_USER_ENTITY);

    }

    @Test
    public void 게시글작성성공() {
        // given
        doReturn(getArticle()).when(articleRepository).save(any(Article.class));

        ArticleCreateRequest articleCreateRequest = getArticleCreateRequest();
        final Member member = getMember();

        // when
        final ArticleCreateResponse result = target.createArticle(articleCreateRequest, member);

        // then
        assertThat(result.getArticleId()).isNotNull();

        // verify
        verify(articleRepository, times(1)).save(any(Article.class));
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void 게시글목록조회성공_사이즈가3() {
        // given
        final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());
        final Member member = getMember();
        Page<Article> pages = new PageImpl<>(Arrays.asList(
                Article.builder().member(member).build(),
                Article.builder().member(member).build(),
                Article.builder().member(member).build()
        ), pageRequest, 3);
        doReturn(pages).when(articleRepository).findAll(pageRequest);

        // when
        final ArticlePageResponse result = target.getArticlePage(pageRequest);

        // then
        assertThat(result.getArticleList().size()).isEqualTo(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    private ArticleCreateRequest getArticleCreateRequest() {
        List<String> labelList = new ArrayList<>();
        labelList.add("Water");
        labelList.add("Sky");
        labelList.add("Cloud");
        labelList.add("Boats and boating--Equipment and supplies");
        labelList.add("Travel");

        List<String> colorList = new ArrayList<>();
        colorList.add("819CBA");
        colorList.add("759BCC");
        colorList.add("69809B");
        colorList.add("A4C2E2");
        colorList.add("405771");

        return ArticleCreateRequest.builder()
                .title("게시글 제목")
                .content("내용")
                .formattedAddress("주소")
                .locationName("장소명")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .lat(38.2252707)
                .lng(128.5883593)
                .labelList(labelList)
                .colorList(colorList)
                .build();
    }

    private Article getArticle() {
        return Article.builder()
                .id(-1L)
                .title("게시글 제목")
                .content("내용")
                .locationName("장소명")
                .formattedAddress("주소")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .build();
    }

    private Member getMember() {
        return Member.builder()
                .id(1L)
                .email("abcdef@gmail.com")
                .nickname("닉네임")
                .build();
    }
}
