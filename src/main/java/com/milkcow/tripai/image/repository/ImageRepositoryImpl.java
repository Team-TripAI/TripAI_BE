package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.dto.ImageResponseData;
import com.milkcow.tripai.image.embedded.Color;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImpl implements ImageRepositoryCustom{
    @Override
    public List<ImageResponseData> searchSimilar(List<String> labelList, List<Color> colorList) {
        //TODO 임시 코드 입니다. 필히 수정해야 합니다.
        return List.of(ImageResponseData.builder().name("test").lat(123).lng(456).address("test address").image("test.com").build());
    }
}
