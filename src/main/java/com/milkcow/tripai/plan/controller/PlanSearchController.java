package com.milkcow.tripai.plan.controller;

import com.milkcow.tripai.global.dto.DataResponse;
import com.milkcow.tripai.plan.dto.accommodation.AccommodationSearchResponseDto;
import com.milkcow.tripai.plan.dto.attraction.AttractionSearchResponseDto;
import com.milkcow.tripai.plan.dto.flight.FlightSearchResponseDto;
import com.milkcow.tripai.plan.dto.restaurant.RestaurantSearchResponseDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanSearchResult;
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
@Api(tags = "PlanSearchController", description = "예산기반 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/plan/budget")
public class PlanSearchController {

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
     * @return {@link FlightSearchResponseDto}
     */
    @ApiOperation(value = "항공권 조회", notes = "날짜에 맞는 항공권을 조회한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departureAirport", value = "출발 공항명(IATA 코드)", required = true, dataType = "string", paramType = "query", example = "ICN"),
            @ApiImplicitParam(name = "arrivalAirport", value = "도착 공항명(IATA 코드)", required = true, dataType = "string", paramType = "query", example = "NRT"),
            @ApiImplicitParam(name = "departure", value = "출발일자(yyyy-MM-dd 형식)", required = true, dataType = "string", paramType = "query", example = "2023-12-25"),
            @ApiImplicitParam(name = "maxFare", value = "최대 항공비", dataType = "int", paramType = "query", example = "1000000")
    })
    @ApiResponses({
            @ApiResponse(code = 410, message = "날짜 형식 오류"),
            @ApiResponse(code = 500, message = "서버 내부 에러"),
            @ApiResponse(code = 510, message = "항공권 API요청 실패"),
            @ApiResponse(code = 540, message = "항공권 API 만료")
    })
    @GetMapping("/flight")
    public DataResponse<FlightSearchResponseDto> getFlightPlan(@RequestParam String departureAirport,
                                                               @RequestParam String arrivalAirport,
                                                               @RequestParam String departure,
                                                               @RequestParam(defaultValue = MAX_INTEGER) int maxFare) {
        if (!DateUtil.isValidDate(departure)) {
            throw new PlanException(PlanSearchResult.INVALID_DATE);
        }
        FlightSearchResponseDto flightData = flightService.getFlightData(departureAirport, arrivalAirport, departure,
                maxFare);
        return DataResponse.create(flightData, PlanSearchResult.OK_FLIGHT_PLAN);
    }

    /**
     * 숙박 정보를 가져온다.
     *
     * @param destination 목적지(영어, 한글 무관)
     * @param startDate   숙박 시작일(yyyy-MM-dd 형식)
     * @param endDate     숙박 종료일(yyyy-MM-dd 형식)
     * @param maxPrice    총 여행기간 중 숙박비
     * @return {@link AccommodationSearchResponseDto}
     */
    @ApiOperation(value = "숙박 정보 조회", notes = "날짜와 가격에 맞는 숙박 정보를 조회한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "destination", value = "목적지(영어, 한글 무관)", required = true, dataType = "string", paramType = "query", example = "도쿄"),
            @ApiImplicitParam(name = "startDate", value = "숙박 시작일(yyyy-MM-dd 형식)", required = true, dataType = "string", paramType = "query", example = "2023-12-25"),
            @ApiImplicitParam(name = "endDate", value = "숙박 종료일(yyyy-MM-dd 형식)", required = true, dataType = "string", paramType = "query", example = "2023-12-31"),
            @ApiImplicitParam(name = "maxPrice", value = "총 여행 기간 중 최대 숙박비", dataType = "int", paramType = "query", example = "100000")
    })
    @ApiResponses({
            @ApiResponse(code = 410, message = "날짜 형식 오류"),
            @ApiResponse(code = 500, message = "서버 내부 에러"),
            @ApiResponse(code = 510, message = "숙박 API 장소 id 검색 요청 실패"),
            @ApiResponse(code = 520, message = "숙박 API 요청 실패"),
            @ApiResponse(code = 540, message = "숙박 API 만료")
    })
    @GetMapping("/accommodation")
    public DataResponse<AccommodationSearchResponseDto> getAccommodationPlan(@RequestParam String destination,
                                                                             @RequestParam String startDate,
                                                                             @RequestParam String endDate,
                                                                             @RequestParam(defaultValue = MAX_INTEGER) int maxPrice) {
        if (!DateUtil.isValidDate(startDate) || !DateUtil.isValidDate(endDate)) {
            throw new PlanException(PlanSearchResult.INVALID_DATE);
        }
        AccommodationSearchResponseDto accommodationData = accommodationService.getAccommodationData(destination,
                startDate,
                endDate, maxPrice);
        return DataResponse.create(accommodationData, PlanSearchResult.OK_ACCOMMODATION_PLAN);
    }


    /**
     * 맛집 정보를 가져온다.
     *
     * @param destination 목적지(영어만 가능, 3글자 이상)
     * @param startDate   여행 시작일(yyyy-MM-dd 형식)
     * @param endDate     여행 종료일(yyyy-MM-dd 형식)
     * @param maxPrice    최대 식비
     * @return {@link RestaurantSearchResponseDto}
     */
    @ApiOperation(value = "맛집 정보 조회", notes = "여행 기간과 예산에 맞는 가격대의 맛집을 조회한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "destination", value = "목적지(영어만, 3글자 이상)", required = true, dataType = "string", paramType = "query", example = "tokyo"),
            @ApiImplicitParam(name = "startDate", value = "여행 시작일(yyyy-MM-dd 형식)", required = true, dataType = "string", paramType = "query", example = "2023-12-25"),
            @ApiImplicitParam(name = "endDate", value = "여행 종료일(yyyy-MM-dd 형식)", required = true, dataType = "string", paramType = "query", example = "2023-12-31"),
            @ApiImplicitParam(name = "maxPrice", value = "총 여행 기간 중 최대 식비", dataType = "int", paramType = "query", example = "100000")
    })
    @ApiResponses({
            @ApiResponse(code = 410, message = "날짜 형식 오류"),
            @ApiResponse(code = 420, message = "유효하지 않은 목적지 형식"),
            @ApiResponse(code = 500, message = "서버 내부 에러"),
            @ApiResponse(code = 510, message = "맛집 API 장소 id 검색 요청 실패"),
            @ApiResponse(code = 520, message = "맛집 API 요청 실패"),
            @ApiResponse(code = 540, message = "맛집 API 만료")
    })
    @GetMapping("/restaurant")
    public DataResponse<RestaurantSearchResponseDto> getRestaurantPlan(@RequestParam String destination,
                                                                       @RequestParam String startDate,
                                                                       @RequestParam String endDate,
                                                                       @RequestParam(defaultValue = MAX_INTEGER) int maxPrice) {
        if (!DateUtil.isValidDate(startDate) || !DateUtil.isValidDate(endDate)) {
            throw new PlanException(PlanSearchResult.INVALID_DATE);
        }

        if (validateRestaurantDestination(destination)) {
            throw new PlanException(PlanSearchResult.INVALID_RESTAURANT_DESTINATION);
        }
        RestaurantSearchResponseDto restaurantData = restaurantService.getRestaurantData(destination, startDate,
                endDate,
                maxPrice);
        return DataResponse.create(restaurantData, PlanSearchResult.OK_RESTAURANT_PLAN);
    }

    /**
     * 해외 명소 정보를 가져온다.
     *
     * @param destination 목적지(영어만 가능, 3글자 이상)
     * @param maxPrice    최대 명소 비용
     * @return {@link AttractionSearchResponseDto}
     */
    @ApiOperation(value = "해외 명소 정보 조회", notes = "여행지별 추천 명소추천 및 예산에 맞는 해당 명소에서 할 수 있는 활동 조회.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "destination", value = "목적지(영어만, 3글자 이상)", required = true, dataType = "string", paramType = "query", example = "tokyo"),
            @ApiImplicitParam(name = "maxPrice", value = "총 여행 기간 중 최대 명소 비용", dataType = "int", paramType = "query", example = "100000")
    })
    @ApiResponses({
            @ApiResponse(code = 420, message = "유효하지 않은 목적지 형식"),
            @ApiResponse(code = 500, message = "서버 내부 에러"),
            @ApiResponse(code = 510, message = "명소 API 장소 id 검색 요청 실패"),
            @ApiResponse(code = 520, message = "명소 API 요청 실패"),
            @ApiResponse(code = 540, message = "명소 API 만료")
    })
    @GetMapping("/attraction/international")
    public DataResponse<AttractionSearchResponseDto> getAttractionPlan(@RequestParam String destination,
                                                                       @RequestParam(defaultValue = MAX_INTEGER) int maxPrice) {
        if (validateRestaurantDestination(destination)) {
            throw new PlanException(PlanSearchResult.INVALID_RESTAURANT_DESTINATION);
        }
        AttractionSearchResponseDto attractionData = attractionService.getAttractionData(destination, maxPrice);
        return DataResponse.create(attractionData, PlanSearchResult.OK_ATTRACTION_PLAN);
    }

    private static boolean validateRestaurantDestination(String destination) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(destination);

        return matcher.find();
    }
}
