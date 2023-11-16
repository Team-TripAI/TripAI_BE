package com.milkcow.tripai.plan.service.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.dto.restaurant.RestaurantSearchData;
import com.milkcow.tripai.plan.dto.restaurant.RestaurantSearchResponseDto;
import com.milkcow.tripai.plan.embedded.PriceRange;
import com.milkcow.tripai.plan.embedded.PlaceHour;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanSearchResult;
import com.milkcow.tripai.plan.util.DateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
 * 맛집 서비스 구현
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Value("${API-keys.Restaurants.Typeahead-URL}")
    private String SEARCH_DESTINATION_URL;
    @Value("${API-keys.Restaurants.Search-URL}")
    private String SEARCH_RESTAURANT_URL;
    @Value("${API-keys.Restaurants.API-Key}")
    private String APIKEY;
    @Value("${API-keys.Restaurants.API-Host}")
    private String APIHOST;

    @Override
    public RestaurantSearchResponseDto getRestaurantData(String destination, String startDate, String endDate, int maxPrice) {
        try {
            long duration = DateUtil.calculateDuration(startDate, endDate);
            PriceRange priceRange = PriceRange.of((int) (maxPrice / duration));

            ArrayList<RestaurantSearchData> restaurantSearchDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = setHeaders();

            String typeaheadBody = setTypeaheadBody(destination);

            HttpEntity<String> typeaheadRequestEntity = new HttpEntity<>(typeaheadBody, headers);

            ResponseEntity<String> typeaheadResponse = restTemplate.exchange(SEARCH_DESTINATION_URL, HttpMethod.POST,
                    typeaheadRequestEntity,
                    String.class);
            if (typeaheadResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanSearchResult.RESTAURANT_DESTINATION_API_REQUEST_FAILED);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode typeaheadJson = objectMapper.readTree(typeaheadResponse.getBody());
            String locationId = typeaheadJson.path("results").path("data").path(0).path("result_object")
                    .get("location_id").asText();

            String restaurantBody = setRestaurantBody(locationId);

            HttpEntity<String> restaurantRequestEntity = new HttpEntity<>(restaurantBody, headers);

            ResponseEntity<String> restaurantResponse = restTemplate.exchange(SEARCH_RESTAURANT_URL, HttpMethod.POST,
                    restaurantRequestEntity,
                    String.class);
            if (restaurantResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanSearchResult.RESTAURANT_SEARCH_API_REQUEST_FAILED);
            }
            JsonNode restaurantJson = objectMapper.readTree(restaurantResponse.getBody());
            JsonNode restaurantList = restaurantJson.path("results").path("data");

            for (JsonNode r : restaurantList) {
                Optional<RestaurantSearchData> restaurantData = parseRestaurantData(r, priceRange);
                restaurantData.ifPresent(restaurantSearchDataList::add);
            }

            return RestaurantSearchResponseDto.builder()
                    .restaurantCount(restaurantSearchDataList.size())
                    .restaurantSearchDataList(restaurantSearchDataList)
                    .build();

        } catch (JsonProcessingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            throw new PlanException(PlanSearchResult.RESTAURANT_API_KEY_LIMIT_EXCESS);
        }
    }

    /**
     * 헤더 설정
     *
     * @return {@link HttpHeaders}
     */
    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/x-www-form-urlencoded");
        headers.add("X-RapidAPI-Key", this.APIKEY);
        headers.add("X-RapidAPI-Host", this.APIHOST);

        return headers;
    }

    /**
     * 목적지 id를 얻기 위한 API body 설정
     *
     * @param destination 목적지(영어만 가능)
     * @return 목적지 id API body
     */
    private static String setTypeaheadBody(String destination) {
        return "q=" + destination +
                "&language=ko_KR";
    }

    /**
     * 맛집 정보를 얻기 위한 API body 설정
     *
     * @param locationId 목적지 id
     * @return 맛집 정보  API body
     */
    private static String setRestaurantBody(String locationId) {
        return "location_id=" + locationId +
                "&language=ko_KR" +
                "&currency=KRW";
    }

    /**
     * 맛집 정보 Json을 객체로 파싱
     *
     * @param restaurant Json
     * @param range      {@link PriceRange} 가격범위 정보
     * @return {@link RestaurantSearchData}
     */
    private static Optional<RestaurantSearchData> parseRestaurantData(JsonNode restaurant, PriceRange range) {
        String price = restaurant.get("price_level").asText();
        int dollarCount = price.split(" - ")[0].length();

        if (!range.isInRange(dollarCount)) {
            return Optional.empty();
        }
        if (!restaurant.has("latitude")) {
            return Optional.empty();
        }

        String name = restaurant.get("name").asText();
        double lat = Double.parseDouble(restaurant.get("latitude").asText());
        double lng = Double.parseDouble(restaurant.get("longitude").asText());

        List<PlaceHour> hourList = PlaceHour.parseHoursList(restaurant);

        String image = restaurant.path("photo").path("images").path("small").get("url").asText();

        RestaurantSearchData restaurantSearchData = RestaurantSearchData.builder()
                .name(name)
                .lat(lat)
                .lng(lng)
                .price(price)
                .hours(hourList)
                .image(image)
                .build();
        return Optional.of(restaurantSearchData);
    }
}

