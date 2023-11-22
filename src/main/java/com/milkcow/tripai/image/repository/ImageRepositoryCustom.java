package com.milkcow.tripai.image.repository;

import com.milkcow.tripai.image.domain.Image;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 조건에 맞는 Image 조회를 위한 리포지토리
 */
@Repository
public interface ImageRepositoryCustom {
    /**
     * 라벨이 하나라도 일치하는 {@link Image} 반환
     * @param labelList 라벨 리스트
     * @return {@link Image} 리스트
     */
    List<Image> searchLabelMatch(List<String> labelList);
}
