package com.milkcow.tripai.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.article.domain.Article;
import com.milkcow.tripai.article.dto.ArticleCreateRequest;
import com.milkcow.tripai.article.dto.ArticleCreateResponse;
import com.milkcow.tripai.article.dto.ArticleDetailResponse;
import com.milkcow.tripai.article.dto.ArticlePageResponse;
import com.milkcow.tripai.article.exception.ArticleException;
import com.milkcow.tripai.article.result.ArticleResult;
import com.milkcow.tripai.article.service.ArticleService;
import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.global.exception.GlobalExceptionHandler;
import com.milkcow.tripai.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {

    @InjectMocks
    private ArticleController target;

    @Mock
    private ArticleService articleService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())

                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void 게시글작성실패_ArticleService에서에러() throws Exception {
        // given
        final String url = "/article";
        String content = objectMapper.writeValueAsString(getArticleCreateRequest());

        doThrow(new ArticleException(ArticleResult.NULL_USER_ENTITY))
                .when(articleService)
                .createArticle(getArticleCreateRequest(), null);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isInternalServerError());
    }

    @ParameterizedTest
    @MethodSource("invalidArticleCreateParameter")
    public void 게시글작성실패_잘못된파라미터(
            final String title,
            final String content,
            final String locationName,
            final String formattedAddress,
            final String image,
            final Double lat,
            final Double lng,
            final List<String> labelList,
            final List<String> colorList
    ) throws Exception {
        // given
        final String url = "/article";
        final ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title(title)
                .content(content)
                .locationName(locationName)
                .formattedAddress(formattedAddress)
                .image(image)
                .lat(lat)
                .lng(lng)
                .labelList(labelList)
                .colorList(colorList)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 게시글작성성공() throws Exception {
        // given
        final String url = "/article";
        final ArticleCreateResponse articleCreateResponse = ArticleCreateResponse.from(1L);

        doReturn(articleCreateResponse).when(articleService)
                .createArticle(any(ArticleCreateRequest.class), any(Member.class));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(objectMapper.writeValueAsString(getArticleCreateRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        DataResponse response = objectMapper.readValue(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), DataResponse.class);

        assertThat(response.getCode()).isEqualTo(201);
        assertThat(response.getSuccess()).isEqualTo(true);
    }

    @Test
    public void 게시글목록조회성공() throws Exception {
        // given
        final String url = "/article";
        final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createDate").descending());

        final Member member = Member.builder().id(1L).nickname("닉네임").build();
        Page<Article> pages = new PageImpl<>(Arrays.asList(
                Article.builder().member(member).build(),
                Article.builder().member(member).build(),
                Article.builder().member(member).build()
        ), pageRequest, 3);

        doReturn(ArticlePageResponse.from(pages)).when(articleService).getArticlePage(pageRequest);

        // when
        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("pageNumber", "0");
        requestParam.set("pageSize", "10");

        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .params(requestParam)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 게시글상세조회성공() throws Exception {
        // given
        final String url = "/article/-1";
        doReturn(ArticleDetailResponse.builder().build()).when(articleService).getArticle(-1L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    private static Stream<Arguments> invalidArticleCreateParameter() {
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

        return Stream.of(
                // Null argument
                Arguments.of(null, "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),
                Arguments.of("제목", null, "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),
                Arguments.of("제목", "내용", null, "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),
                Arguments.of("제목", "내용", "주소", null,
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),
                Arguments.of("제목", "내용", "주소", "장소명",
                        null, 38.2252707, 128.5883593, labelList, colorList),
                Arguments.of("제목", "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", null, 128.5883593, labelList, colorList),
                Arguments.of("제목", "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, null, labelList, colorList),
                Arguments.of("제목", "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, null, colorList),
                Arguments.of("제목", "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, null),

                // 제목 글자수 제한 초과
                Arguments.of("제".repeat(20 + 1), "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),
                // 제목 공백
                Arguments.of("     ", "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),
                // 제목 빈 문자열
                Arguments.of("", "내용", "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList),

                // 내용 글자수 제한 초과
                Arguments.of("제목", "내".repeat(500 + 1), "주소", "장소명",
                        "36b8f84d-df4e-4d49-b662-bcde71a8764f", 38.2252707, 128.5883593, labelList, colorList)
        );
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
}
