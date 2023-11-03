package com.milkcow.tripai.plan.service.accommodation;

import com.milkcow.tripai.plan.dto.AccommodationDataDto;

public interface AccommodationService {
    AccommodationDataDto getAccommodationData(String destination, String startDate, String endDate, int maxPrice);
}
