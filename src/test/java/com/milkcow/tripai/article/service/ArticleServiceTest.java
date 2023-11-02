package com.milkcow.tripai.article.service;

import com.milkcow.tripai.article.dto.*;
import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.repository.ArticleRepository;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.exception.ImageException;
import com.milkcow.tripai.image.repository.ImageRepository;
import com.milkcow.tripai.image.result.ImageResult;
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
import java.util.Optional;

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
                () -> target.create(request, null));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.NULL_USER_ENTITY);

    }

    @Test
    public void 게시글작성성공() {
        // given
        ArticleCreateRequest articleCreateRequest = getArticleCreateRequest();
        final Member member = getMember(-1L);

        doReturn(getArticle(member)).when(articleRepository).save(any(Article.class));

        // when
        final ArticleCreateResponse result = target.create(articleCreateRequest, member);

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
        final Member member = getMember(-1L);
        Page<Article> pages = new PageImpl<>(Arrays.asList(
                Article.builder().member(member).build(),
                Article.builder().member(member).build(),
                Article.builder().member(member).build()
        ), pageRequest, 3);
        doReturn(pages).when(articleRepository).findAll(pageRequest);

        // when
        final ArticlePageResponse result = target.getPage(pageRequest);

        // then
        assertThat(result.getArticleList().size()).isEqualTo(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    public void 게시글상세조회실패_존재하지않음() {
        // given
        doReturn(Optional.empty()).when(articleRepository).findById(anyLong());

        // when
        final ArticleException result = assertThrows(ArticleException.class, () -> target.getDetail(-1L));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.ARTICLE_NOT_FOUND);
    }

    @Test
    public void 게시글상세조회성공() {
        // given
        Member member = getMember(-1L);
        doReturn(Optional.of(getArticle(member))).when(articleRepository).findById(anyLong());

        // when
        final ArticleDetailResponse result = target.getDetail(-1L);

        // then
        assertThat(result.getArticleId()).isNotNull();
    }

    @Test
    public void 게시글수정실패_게시글이존재하지않음() {
        // given
        ArticleModifyRequest request = getArticleModifyRequest();
        Member member = getMember(-1L);
        doReturn(Optional.empty()).when(articleRepository).findById(anyLong());

        // when
        final ArticleException result = assertThrows(ArticleException.class,
                () -> target.modify(-1L, request, member));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.ARTICLE_NOT_FOUND);
    }

    @Test
    public void 게시글수정실패_이미지가존재하지않음() {
        // given
        ArticleModifyRequest request = getArticleModifyRequest();
        Member member = getMember(-1L);
        doReturn(Optional.of(getArticle(member))).when(articleRepository).findById(anyLong());
        doReturn(Optional.empty()).when(imageRepository).findByImage(anyString());

        // when
        final ImageException result = assertThrows(ImageException.class,
                () -> target.modify(-1L, request, member));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ImageResult.IMAGE_NOT_FOUND);
    }

    @Test
    public void 게시글수정실패_MEMBER가NULL임() {
        // given
        ArticleModifyRequest request = getArticleModifyRequest();
        doReturn(Optional.of(Article.builder().build())).when(articleRepository).findById(anyLong());

        // when
        final ArticleException result = assertThrows(ArticleException.class,
                () -> target.modify(-1L, request, null));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.NULL_USER_ENTITY);
    }

    @Test
    public void 게시글수정실패_작성자가아님() {
        // given
        ArticleModifyRequest request = getArticleModifyRequest();
        Member member1 = getMember(-1L);
        Member member2 = getMember(-2L);
        doReturn(Optional.of(getArticle(member1))).when(articleRepository).findById(anyLong());

        // when
        final ArticleException result = assertThrows(ArticleException.class,
                () -> target.modify(-1L, request, member2));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.NOT_ARTICLE_OWNER);
    }

    @Test
    public void 게시글수정성공() {
        // given
        Member member = getMember(-1L);
        ArticleModifyRequest request = getArticleModifyRequest();
        doReturn(Optional.of(getArticle(member))).when(articleRepository).findById(anyLong());
        doReturn(Optional.of(Image.builder().build())).when(imageRepository).findByImage(anyString());

        // when
        final ArticleModifyResponse result = target.modify(-1L, request, member);

        // then
        assertThat(result.getArticleId()).isNotNull();
    }

    @Test
    public void 게시글삭제실패_MEMBER가NULL임() {
        // given
        doReturn(Optional.of(Article.builder().build())).when(articleRepository).findById(anyLong());

        // when
        final ArticleException result = assertThrows(ArticleException.class, () -> target.remove(-1L, null));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.NULL_USER_ENTITY);
    }

    @Test
    public void 게시글삭제실패_게시글이존재하지않음() {
        // given
        doReturn(Optional.empty()).when(articleRepository).findById(anyLong());
        Member member = getMember(-1L);

        // when
        final ArticleException result = assertThrows(ArticleException.class, () -> target.remove(-1L, member));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.ARTICLE_NOT_FOUND);
    }

    @Test
    public void 게시글삭제실패_작성자가아님() {
        // given
        Member member1 = getMember(-1L);
        Member member2 = getMember(-2L);
        doReturn(Optional.of(getArticle(member1))).when(articleRepository).findById(anyLong());

        // when
        final ArticleException result = assertThrows(ArticleException.class, () -> target.remove(-1L, member2));

        // then
        assertThat(result.getErrorResult()).isEqualTo(ArticleResult.NOT_ARTICLE_OWNER);
    }

    @Test
    public void 게시글삭제성공() {
        // given
        Member member = getMember(-1L);
        doReturn(Optional.of(getArticle(member))).when(articleRepository).findById(anyLong());

        // when
        target.remove(-1L, member);

        // then
    }



    private ArticleModifyRequest getArticleModifyRequest() {
        List<String> labelList = getLabelList();
        List<String> colorList = getColorList();

        return ArticleModifyRequest.builder()
                .title("새 게시글 제목")
                .content("새 내용")
                .formattedAddress("새 주소")
                .locationName("새 장소명")
                .image("000000-df4e-4d49-b662-bcde71a8764f")
                .lat(38.010101)
                .lng(128.010101)
                .labelList(labelList)
                .colorList(colorList)
                .build();
    }

    private ArticleCreateRequest getArticleCreateRequest() {
        List<String> labelList = getLabelList();

        List<String> colorList = getColorList();

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

    private List<String> getColorList() {
        List<String> colorList = new ArrayList<>();
        colorList.add("819CBA");
        colorList.add("759BCC");
        colorList.add("69809B");
        colorList.add("A4C2E2");
        colorList.add("405771");
        return colorList;
    }

    private List<String> getLabelList() {
        List<String> labelList = new ArrayList<>();
        labelList.add("Water");
        labelList.add("Sky");
        labelList.add("Cloud");
        labelList.add("Boats and boating--Equipment and supplies");
        labelList.add("Travel");
        return labelList;
    }

    private Article getArticle(Member member) {
        return Article.builder()
                .id(-1L)
                .title("게시글 제목")
                .content("내용")
                .member(member)
                .locationName("장소명")
                .formattedAddress("주소")
                .image("36b8f84d-df4e-4d49-b662-bcde71a8764f")
                .build();
    }

    private Member getMember(Long id) {
        return Member.builder()
                .id(id)
                .email("abcdef@gmail.com")
                .nickname("닉네임")
                .build();
    }
}
