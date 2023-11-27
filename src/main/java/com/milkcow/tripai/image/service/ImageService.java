package com.milkcow.tripai.image.service;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import com.milkcow.tripai.image.dto.ImageResponseData;
import com.milkcow.tripai.image.dto.ImageResponseDto;
import com.milkcow.tripai.image.embedded.Color;
import com.milkcow.tripai.image.exception.ImageException;
import com.milkcow.tripai.image.repository.ImageRepositoryCustom;
import com.milkcow.tripai.image.result.ImageResult;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 이미지 기반 추천 서비스
 */
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepositoryCustom imageRepository;

    /**
     * 이미지 기반 유사 장소 추천
     *
     * @param requestDto {@link ImageRequestDto}
     * @return {@link ImageResponseData}
     */
    public DataResponse<ImageResponseDto> searchSimilarPlace(ImageRequestDto requestDto) {
        List<String> requestLabelList = requestDto.getLabelList();
        List<Color> requestColorList = requestDto.stringToColor();

        List<Image> labelMatchedList = imageRepository.searchLabelMatch(requestLabelList);

        List<ImageResponseData> recommendList = calculateSimilarImage(requestLabelList, requestColorList,
                labelMatchedList);

        int recommendCount = recommendList.size();
        ImageResponseDto responseDto = ImageResponseDto
                .builder()
                .recommendCount(recommendCount)
                .recommendList(recommendList)
                .build();
        return DataResponse.create(responseDto, ImageResult.OK_IMAGE_BASED_PLACE);
    }

    /**
     * 사용자 요청 라벨, 색상과 image별 유사도 점수 내림차순 정렬, 최대 8개 반환
     * @param requestLabelList 사용자 요청 라벨 리스트
     * @param requestColorList 사용자 요청 색상 리스트
     * @param labelMatchedList 사용자 요청 라벨리스트 중 하나이상 일치하는 image 리스트
     * @return {@link ImageResponseData}
     */
    private List<ImageResponseData> calculateSimilarImage(List<String> requestLabelList,
                                                          List<Color> requestColorList,
                                                          List<Image> labelMatchedList) {
        List<ImageResponseData> toatalDataList = labelMatchedList
                .stream()
                .map(i -> new ImageScore(requestLabelList, requestColorList, i))
                .sorted(Comparator.comparing(ImageScore::getScore).reversed())
                .map(ImageScore::toDto)
                .collect(Collectors.toList());

        if (toatalDataList.size() >= 8) {
            return toatalDataList.subList(0, 8);
        }

        if (toatalDataList.isEmpty()){
            throw new ImageException(ImageResult.IMAGE_NOT_FOUND);
        }

        return toatalDataList;
    }
}
