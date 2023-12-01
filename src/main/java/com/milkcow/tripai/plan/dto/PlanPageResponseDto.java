package com.milkcow.tripai.plan.dto;


import com.milkcow.tripai.plan.domain.Plan;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@RequiredArgsConstructor
@Builder
public class PlanPageResponseDto {

    @ApiParam(value = "여행일정 목록")
    @Schema(description = "조회된 여행일정 목록")
    private final List<PlanResponseDto> planList;

    @ApiParam(value = "총 페이지 수")
    @Schema(description = "총 페이지 수", example = "1")
    private final int totalPages;

    @ApiParam(value = "모든 페이지의 총 여행일정 수")
    @Schema(description = "모든 페이지의 총 여행일정 수", example = "1")
    private final long totalElements;

    @ApiParam(value = "현재 페이지가 첫 페이지인지 여부")
    @Schema(description = "현재 페이지가 첫 페이지인지 여부", example = "true")
    private final boolean first;

    @ApiParam(value = "현재 페이지가 마지막 페이지인지 여부")
    @Schema(description = "현재 페이지가 마지막 페이지인지 여부", example = "true")
    private final boolean last;

    @ApiParam(value = "현재 페이지 번호")
    @Schema(description = "현재 페이지 번호", example = "0")
    private final int number;

    @ApiParam(value = "페이지 당 조회될 여행일정 수")
    @Schema(description = "페이지 당 조회될 여행일정 수", example = "10")
    private final int size;

    @ApiParam(value = "실제로 조회된 여행일정 수")
    @Schema(description = "실제로 조회된 여행일정 수", example = "1")
    private final int numberOfElements;

    public static PlanPageResponseDto toDto(Page<Plan> planPage) {
        return PlanPageResponseDto.builder()
                .totalPages(planPage.getTotalPages())
                .totalElements(planPage.getTotalElements())
                .first(planPage.isFirst())
                .last(planPage.isLast())
                .number(planPage.getNumber())
                .size(planPage.getSize())
                .numberOfElements(planPage.getNumberOfElements())
                .planList(planPage.getContent()
                        .stream()
                        .map(plan -> PlanResponseDto.toDto(plan))
                        .collect(Collectors.toList()))
                .build();
    }

}
