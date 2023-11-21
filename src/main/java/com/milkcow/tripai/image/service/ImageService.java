package com.milkcow.tripai.image.service;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.image.domain.Image;
import com.milkcow.tripai.image.dto.ImageRequestDto;
import com.milkcow.tripai.image.dto.ImageResponseData;
import com.milkcow.tripai.image.dto.ImageResponseDto;
import com.milkcow.tripai.image.repository.ImageRepositoryCustom;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepositoryCustom imageRepository;

    public DataResponse<ImageResponseDto> searchSimilarPlace(ImageRequestDto requestDto) {
        //TODO 임시 코드입니다. 리포지토리에서 받은 임의의 값을 그대로 사용합니다.
        // 추천 알고리즘 작성해주세요~
        List<Image> labelMatchedList = imageRepository.searchSimilar(requestDto.getLabelList(),
                requestDto.stringToColor());
        
        List<ImageResponseData> recommendList = labelMatchedList.stream().map(ImageResponseData::toDto)
                .collect(Collectors.toList());

        int recommendCount = recommendList.size();
        ImageResponseDto responseDto = ImageResponseDto
                .builder()
                .recommendCount(recommendCount)
                .recommendList(recommendList)
                .build();
        return DataResponse.create(responseDto);
    }
}
