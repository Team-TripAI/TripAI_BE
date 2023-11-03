package com.milkcow.tripai.plan.service;

import com.milkcow.tripai.plan.dto.AccommodationDataDto;

public interface AccommodationPlanService {
    AccommodationDataDto getAccommodationData(String destination, String startDate, String endDate, int maxPrice);
}
