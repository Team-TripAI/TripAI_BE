package com.milkcow.tripai.plan.service.attraction;

import com.milkcow.tripai.plan.dto.AttractionDataDto;

/**
 * 명소 정보를 가져오는 service
 */
public interface AttractionService {
    /**
     * 명소 정보를 가져오는 메서드
     *
     * @param destination 목적지
     * @param maxPrice    최대 명소 비용
     * @return {@link AttractionDataDto}
     */
    AttractionDataDto getAttractionData(String destination, int maxPrice);
}
