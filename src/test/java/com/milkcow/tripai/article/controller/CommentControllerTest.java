package com.milkcow.tripai.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.article.dto.CommentCreateRequest;
import com.milkcow.tripai.article.dto.CommentCreateResponse;
import com.milkcow.tripai.article.service.CommentService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @InjectMocks
    private CommentController target;

    @Mock
    private CommentService commentService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())

                .build();
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @MethodSource("invalidCommentParameter")
    public void 댓글작성실패_잘못된파라미터(Long articleId, Long commentId, String content) throws Exception {
        // given
        final String url = "/comment";
        CommentCreateRequest request = CommentCreateRequest.builder()
                .articleId(articleId)
                .commentId(commentId)
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 댓글작성성공() throws Exception {
        // given
        final String url = "/comment";
        CommentCreateRequest request = CommentCreateRequest.builder()
                .articleId(-1L)
                .content("댓글 내용")
                .build();

        doReturn(CommentCreateResponse.from(-1L))
                .when(commentService).createComment(any(CommentCreateRequest.class), any(Member.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());
    }

    private static Stream<Arguments> invalidCommentParameter() {
        return Stream.of(
            Arguments.of(null, null, "댓글 내용"),
            Arguments.of(-1L, null, null)
        );
    }
}