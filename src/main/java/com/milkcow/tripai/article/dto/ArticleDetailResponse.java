package com.milkcow.tripai.article.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.milkcow.tripai.article.domain.Article;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class ArticleDetailResponse {

    @ApiParam(value = "게시글 ID")
    @Schema(description = "조회된 게시글 ID", example = "1")
    private final Long articleId;

    @ApiParam(value = "게시글 제목")
    @Schema(description = "작성된 게시글 제목", example = "속초 배낚시")
    private final String title;

    @ApiParam(value = "게시글 내용")
    @Schema(description = "작성된 게시글 내용", example = "해초 전문 낚시꾼 박지안")
    private final String content;

    @ApiParam(value = "여행장소 이름")
    @Schema(description = "여행장소 이름", example = "속초배낚시체험 모래기호")
    private final String locationName;

    @ApiParam(value = "여행장소 주소")
    @Schema(description = "여행장소 주소", example = "대한민국 강원도 속초시 장사항해안길 41")
    private final String formattedAddress;

    @ApiParam(value = "여행사진")
    @Schema(description = "여행사진", example = "해초1699197018587.png")
    private final String image;

    @ApiParam(value = "작성자 이름")
    @Schema(description = "작성자 이름", example = "박수영")
    private final String nickname;

    @ApiParam(value = "작성일자")
    @Schema(description = "작성일자", example = "2023-10-09T23:30:15.123456")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createDate;

    @ApiParam(value = "최종수정일자")
    @Schema(description = "최종수정일자", example = "2023-10-09T23:57:45.123456")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifyDate;

    @ApiParam(value = "댓글 목록")
    @Schema(description = "댓글 목록", example = "[\n" +
            "\t{\n" +
            "\t\t\"commentId\": 1,\n" +
            "\t\t\"content\": \"이야 해초 보소\",\n" +
            "\t\t\"isParent\": true,\n" +
            "\t\t\"nickname\": \"정준서\",\n" +
            "\t\t\"createDate\": \"2023-10-31T01:01:22.561065\",\n" +
            "\t\t\"modifyDate\": \"2023-10-31T01:01:22.561065\"\n" +
            "\t}\n" +
            "]")
    private final List<CommentDetailResponse> commentList;

    public static ArticleDetailResponse of(Article article, List<CommentSearch> searchedComments) {
        List<CommentDetailResponse> commentList = searchedComments.stream()
                .map(CommentDetailResponse::from)
                .collect(Collectors.toList());

        return ArticleDetailResponse.builder()
                .articleId(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .locationName(article.getLocationName())
                .formattedAddress(article.getFormattedAddress())
                .image(article.getImage())
                .nickname(article.getMember().getNickname())
                .createDate(article.getCreateDate())
                .modifyDate(article.getModifyDate())
                .commentList(commentList)
                .build();
    }
}
