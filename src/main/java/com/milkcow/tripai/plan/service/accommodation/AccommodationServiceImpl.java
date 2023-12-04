package com.milkcow.tripai.plan.service.accommodation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.dto.accommodation.AccommodationSearchData;
import com.milkcow.tripai.plan.dto.accommodation.AccommodationSearchResponseDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanSearchResult;
import com.milkcow.tripai.plan.util.DateUtil;
import java.util.ArrayList;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * 숙박 서비스 구현
 */
@Service
public class AccommodationServiceImpl implements AccommodationService {

    @Value("${API-keys.Booking.SearchHotelDestination-URL}")
    private String SEARCH_DESTINATION_URL;
    @Value("${API-keys.Booking.SearchHotels-URL}")
    private String SEARCH_HOTELS_URL;
    @Value("${API-keys.Booking.API-Key}")
    private String APIKEY;
    @Value("${API-keys.Booking.API-Host}")
    private String APIHOST;

    @Override
    public AccommodationSearchResponseDto getAccommodationData(String destination, String startDate, String endDate,
                                                               int maxPrice) {
        try {
            ArrayList<AccommodationSearchData> accommodationSearchDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = setHeaders();

            String searchDestinationUrl = setDestinationURL(destination);

            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> destinationResponse = restTemplate.exchange(searchDestinationUrl, HttpMethod.GET,
                    requestEntity,
                    String.class);

            if (destinationResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanSearchResult.ACCOMMODATION_DESTINATION_API_REQUEST_FAILED);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode destinationJson = objectMapper.readTree(destinationResponse.getBody());
            long destId = destinationJson.path("data").path(0).get("dest_id").asLong();

            String hotelURL = setHotelURL(destId, startDate, endDate, maxPrice);

            ResponseEntity<String> hotelResponse = restTemplate.exchange(hotelURL, HttpMethod.GET, requestEntity,
                    String.class);

            if (destinationResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanSearchResult.ACCOMMODATION_SEARCH_API_REQUEST_FAILED);
            }
            JsonNode accommodationJson = objectMapper.readTree(hotelResponse.getBody());

            if (!accommodationJson.get("status").asBoolean()) {
                throw new PlanException(PlanSearchResult.ACCOMMODATION_DESTINATION_API_REQUEST_FAILED);
            }
            JsonNode accommodationList = accommodationJson.path("data").path("hotels");

            for (JsonNode acc : accommodationList) {
                AccommodationSearchData accommodationSearchData = parseAccommodationData(acc, startDate, endDate);
                accommodationSearchDataList.add(accommodationSearchData);
            }

            return AccommodationSearchResponseDto.builder()
                    .AccommodationCount(accommodationSearchDataList.size())
                    .accommodationSearchDataList(accommodationSearchDataList)
                    .build();

        } catch (JsonProcessingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            throw new PlanException(PlanSearchResult.ACCOMMODATION_API_KEY_LIMIT_EXCESS);
        }
    }

    /**
     * 숙박정보 Json을 객체로 파싱
     *
     * @param accommodation Json
     * @param startDate     숙박 시작일(yyyy-MM-dd 형식)
     * @param endDate       숙박 종료일(yyyy-MM-dd 형식)
     * @return AccommodationData {@link AccommodationSearchData}
     */
    private static AccommodationSearchData parseAccommodationData(JsonNode accommodation, String startDate,
                                                                  String endDate) {

        String name = accommodation.path("property").get("name").asText();
        double lat = accommodation.path("property").get("latitude").asDouble();
        double lng = accommodation.path("property").get("longitude").asDouble();
        long price = accommodation.path("property").path("priceBreakdown").path("grossPrice").get("value")
                .asLong();
        String image = accommodation.path("property").path("photoUrls").path(0).asText();

        long duration = DateUtil.calculateDuration(startDate, endDate);

        return AccommodationSearchData.builder()
                .name(name)
                .lat(lat)
                .lng(lng)
                .startDate(startDate)
                .endDate(endDate)
                .price(price)
                .avgPrice(price / duration)
                .image(image)
                .build();
    }


    /**
     * 헤더 설정
     *
     * @return {@link HttpHeaders}
     */
    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", this.APIKEY);
        headers.add("X-RapidAPI-Host", this.APIHOST);

        return headers;
    }

    /**
     * 목적지 id를 얻기 위한 API URL 설정
     *
     * @param destination 목적지
     * @return 목적지 id API URL
     */
    private String setDestinationURL(String destination) {
        return SEARCH_DESTINATION_URL + "?query=" + destination;
    }

    /**
     * 호텔 정보를 얻기 위한 API URL 설정
     *
     * @param destId    목적지 id
     * @param startDate 숙박 시작일
     * @param endDate   숙박 종료일
     * @param maxPrice  총 여행기간 중 숙박비
     * @return 호텔 정보 API URL
     */
    private String setHotelURL(long destId, String startDate, String endDate, int maxPrice) {
        String url = SEARCH_HOTELS_URL
                + "?dest_id=" + destId
                + "&search_type=CITY"
                + "&arrival_date=" + startDate
                + "&departure_date=" + endDate
                + "&adults=1"
                + "&room_qty=1"
                + "&page_number=1"
                + "&price_max=" + maxPrice
                + "&sort_by=popularity"
                + "&units=metric"
                + "&temperature_unit=c"
                + "&languagecode=ko"
                + "&currency_code=KRW";
        return url;
    }
}
