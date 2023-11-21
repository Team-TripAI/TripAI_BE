package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.dto.ImageResponseData;
import com.milkcow.tripai.image.embedded.Color;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepositoryCustom {
    List<ImageResponseData> searchSimilar(List<String> labelList, List<Color> colorList);
}
