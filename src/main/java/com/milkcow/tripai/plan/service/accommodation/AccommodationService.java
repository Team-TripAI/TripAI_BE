package com.milkcow.tripai.plan.service.accommodation;

import com.milkcow.tripai.plan.dto.accommodation.AccommodationSearchResponseDto;

/**
 * 숙박 정보를 가져오는 Service 인터페이스
 */
public interface AccommodationService {
    /**
     * 숙박정보를 가져오는 메서드
     * @param destination 목적지(영어, 한글 무관)
     * @param startDate 숙박 시작일(yyyy-MM-dd 형식)
     * @param endDate 숙박 종료일(yyyy-MM-dd 형식)
     * @param maxPrice 총 여행기간 중 숙박비
     * @return {@link AccommodationSearchResponseDto}
     */
    AccommodationSearchResponseDto getAccommodationData(String destination, String startDate, String endDate, int maxPrice);
}
