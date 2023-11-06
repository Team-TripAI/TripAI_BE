package com.milkcow.tripai.plan.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.plan.dto.AccommodationDataDto;
import com.milkcow.tripai.plan.dto.AttractionDataDto;
import com.milkcow.tripai.plan.dto.FlightDataDto;
import com.milkcow.tripai.plan.dto.RestaurantDataDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanGetResult;
import com.milkcow.tripai.plan.service.accommodation.AccommodationService;
import com.milkcow.tripai.plan.service.attraction.AttractionService;
import com.milkcow.tripai.plan.service.flight.FlightService;
import com.milkcow.tripai.plan.service.restaurant.RestaurantService;
import com.milkcow.tripai.plan.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예산기반 여행지 추천 controller
 */
@Api(tags = "PlanController", description = "예산기반 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/plan/budget")
public class PlanController {

    private final FlightService flightService;
    private final AccommodationService accommodationService;
    private final RestaurantService restaurantService;
    private final AttractionService attractionService;

    private final static String MAX_INTEGER = "2147483647";

    /**
     * 항공권 정보를 가져온다.
     *
     * @param departureAirport 출발 공항 명(IATA 코드)
     * @param arrivalAirport   도착 공항 명(IATA 코드)
     * @param departure        출발 일자(yyyy-MM-dd 형식)
     * @param maxFare          최대 항공비
     * @return {@link FlightDataDto}
     */
    @ApiOperation(value = "항공권 조회", notes = "날짜에 맞는 항공권을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 400, message = "날짜 형식 오류"),
            @ApiResponse(code = 500, message = "서버 내부 에러"),
            @ApiResponse(code = 510, message = "항공권 API요청 실패"),
            @ApiResponse(code = 540, message = "항공권 API 만료")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departureAirport", value = "출발 공항명(IATA 코드)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "arrivalAirport", value = "도착 공항명(IATA 코드)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "departure", value = "출발일자(yyyy-MM-dd 형식)", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "maxFare", value = "최대 항공비", dataType = "int", paramType = "query")
    })
    @GetMapping("/flight")
    public DataResponse<FlightDataDto> getFlightPlan(@RequestParam String departureAirport,
                                                     @RequestParam String arrivalAirport,
                                                     @RequestParam String departure,
                                                     @RequestParam(defaultValue = MAX_INTEGER) int maxFare) {
        if (!DateUtil.isValidDate(departure)) {
            throw new PlanException(PlanGetResult.INVALID_DATE);
        }
        FlightDataDto flightData = flightService.getFlightData(departureAirport, arrivalAirport, departure,
                maxFare);
        return DataResponse.create(flightData, PlanGetResult.OK_FLIGHT_PLAN);
    }

    /**
     * 숙박 정보를 가져온다.
     *
     * @param destination 목적지(영어, 한글 무관)
     * @param startDate   숙박 시작일(yyyy-MM-dd 형식)
     * @param endDate     숙박 종료일(yyyy-MM-dd 형식)
     * @param maxPrice    총 여행기간 중 숙박비
     * @return {@link AccommodationDataDto}
     */
    @GetMapping("/accommodation")
    public DataResponse<AccommodationDataDto> getAccommodationPlan(@RequestParam String destination,
                                                                   @RequestParam String startDate,
                                                                   @RequestParam String endDate,
                                                                   @RequestParam(defaultValue = MAX_INTEGER) int maxPrice) {
        if (!DateUtil.isValidDate(startDate) || !DateUtil.isValidDate(endDate)) {
            throw new PlanException(PlanGetResult.INVALID_DATE);
        }
        AccommodationDataDto accommodationData = accommodationService.getAccommodationData(destination, startDate,
                endDate, maxPrice);
        return DataResponse.create(accommodationData, PlanGetResult.OK_ACCOMMODATION_PLAN);
    }


    /**
     * 맛집 정보를 가져온다.
     *
     * @param destination 목적지(영어만 가능, 3글자 이상)
     * @param startDate   여행 시작일(yyyy-MM-dd 형식)
     * @param endDate     여행 종료일(yyyy-MM-dd 형식)
     * @param maxPrice    최대 식비
     * @return {@link RestaurantDataDto}
     */
    @GetMapping("/restaurant")
    public DataResponse<RestaurantDataDto> getRestaurantPlan(@RequestParam String destination,
                                                             @RequestParam String startDate,
                                                             @RequestParam String endDate,
                                                             @RequestParam(defaultValue = MAX_INTEGER) int maxPrice) {
        if (!DateUtil.isValidDate(startDate) || !DateUtil.isValidDate(endDate)) {
            throw new PlanException(PlanGetResult.INVALID_DATE);
        }

        if (validateRestaurantDestination(destination)) {
            throw new PlanException(PlanGetResult.INVALID_RESTAURANT_DESTINATION);
        }
        RestaurantDataDto restaurantData = restaurantService.getRestaurantData(destination, startDate, endDate,
                maxPrice);
        return DataResponse.create(restaurantData, PlanGetResult.OK_RESTAURANT_PLAN);
    }

    /**
     * 해외 명소 정보를 가져온다.
     *
     * @param destination 목적지(영어만 가능, 3글자 이상)
     * @param maxPrice    최대 명소 비용
     * @return {@link AttractionDataDto}
     */
    @GetMapping("/attraction/international")
    public DataResponse<AttractionDataDto> getAttractionPlan(@RequestParam String destination,
                                                             @RequestParam(defaultValue = MAX_INTEGER) int maxPrice) {
        if (validateRestaurantDestination(destination)) {
            throw new PlanException(PlanGetResult.INVALID_RESTAURANT_DESTINATION);
        }
        AttractionDataDto attractionData = attractionService.getAttractionData(destination, maxPrice);
        return DataResponse.create(attractionData, PlanGetResult.OK_ATTRACTION_PLAN);
    }

    private static boolean validateRestaurantDestination(String destination) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(destination);

        return matcher.find();
    }
}
