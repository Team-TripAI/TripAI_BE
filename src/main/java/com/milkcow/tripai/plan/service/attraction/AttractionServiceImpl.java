package com.milkcow.tripai.plan.service.attraction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.embedded.AttractionData;
import com.milkcow.tripai.plan.dto.AttractionDataDto;
import com.milkcow.tripai.plan.embedded.AttractionOffer;
import com.milkcow.tripai.plan.embedded.PlaceHour;
import com.milkcow.tripai.plan.embedded.PriceRange;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanGetResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
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
 * 명소 서비스 구현
 */
@Service
public class AttractionServiceImpl implements AttractionService {

    @Value("${API-keys.Tourist-Attraction.Typeahead-URL}")
    private String SEARCH_DESTINATION_URL;
    @Value("${API-keys.Tourist-Attraction.Search-URL}")
    private String SEARCH_ATTRACTION_URL;
    @Value("${API-keys.Tourist-Attraction.API-Key}")
    private String APIKEY;
    @Value("${API-keys.Tourist-Attraction.API-Host}")
    private String APIHOST;

    @Override
    public AttractionDataDto getAttractionData(String destination, int maxPrice) {
        try {
            ArrayList<AttractionData> attractionDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = setHeaders();

            String typeaheadBody = setTypeaheadBody(destination);

            HttpEntity<String> typeaheadRequestEntity = new HttpEntity<>(typeaheadBody, headers);

            ResponseEntity<String> typeaheadResponse = restTemplate.exchange(SEARCH_DESTINATION_URL, HttpMethod.POST,
                    typeaheadRequestEntity, String.class);
            if (typeaheadResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanGetResult.ATTRACTION_DESTINATION_API_REQUEST_FAILED);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode typeaheadJson = objectMapper.readTree(typeaheadResponse.getBody());
            String locationId = typeaheadJson.path("results").path("data").path(0).path("result_object")
                    .get("location_id").asText();

            String attractionBody = setAttractionBody(locationId);

            HttpEntity<String> attractionRequestEntity = new HttpEntity<>(attractionBody, headers);

            ResponseEntity<String> attractionResponse = restTemplate.exchange(SEARCH_ATTRACTION_URL, HttpMethod.POST,
                    attractionRequestEntity, String.class);
            if (attractionResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanGetResult.ATTRACTION_SEARCH_API_REQUEST_FAILED);
            }
            JsonNode attractionJson = objectMapper.readTree(attractionResponse.getBody());
            JsonNode attractionList = attractionJson.path("results").path("data");

            for (JsonNode a : attractionList) {
                Optional<AttractionData> attractionData = parseRestaurantData(a, maxPrice);
                attractionData.ifPresent(attractionDataList::add);
            }

            return AttractionDataDto.builder()
                    .AttractionCount(attractionList.size())
                    .attractionDataList(attractionDataList)
                    .build();

        } catch (JsonProcessingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            throw new PlanException(PlanGetResult.ATTRACTION_API_KEY_LIMIT_EXCESS);
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
     * 명소 정보를 얻기 위한 API body 설정
     *
     * @param locationId 목적지 id
     * @return 맛집 정보  API body
     */
    private static String setAttractionBody(String locationId) {
        return "location_id=" + locationId +
                "&language=en_US" +
                "&currency=KRW";
    }

    /**
     * 명소 정보 Json을 객체로 파싱
     *
     * @param attraction Json
     * @param maxPrice   최대 명소 비용
     * @return {@link AttractionData}
     */
    private static Optional<AttractionData> parseRestaurantData(JsonNode attraction, int maxPrice) {

        if (!attraction.has("latitude")) {
            return Optional.empty();
        }

        int dollarCount = 0;

        String name = attraction.get("name").asText();
        double lat = Double.parseDouble(attraction.get("latitude").asText());
        double lng = Double.parseDouble(attraction.get("longitude").asText());

        List<AttractionOffer> offerList = parseOffer(attraction, maxPrice);

        OptionalDouble avgPrice = offerList.stream()
                .mapToInt(AttractionOffer::getOfferPrice)
                .average();

        if (avgPrice.isPresent()) {
            PriceRange priceRange = PriceRange.of((int) avgPrice.getAsDouble());
            dollarCount = priceRange.getMaxDollar();
        }

        List<PlaceHour> hourList = PlaceHour.parseHoursList(attraction);
        String image = attraction.path("photo").path("images").path("small").get("url").asText();

        AttractionData attractionData = AttractionData.builder()
                .name(name)
                .lat(lat)
                .lng(lng)
                .offerCount(offerList.size())
                .offerList(offerList)
                .price("$".repeat(dollarCount))
                .hours(hourList)
                .image(image)
                .build();

        return Optional.of(attractionData);
    }

    private static List<AttractionOffer> parseOffer(JsonNode attraction, int maxPrice) {
        if (!attraction.has("offer_group")) {
            return Collections.emptyList();
        }
        ArrayList<AttractionOffer> offerList = new ArrayList<>();
        JsonNode offerListJson = attraction.path("offer_group").get("offer_list");

        for (JsonNode o : offerListJson) {
            int price = Integer.parseInt(o.get("rounded_up_price").asText().replace("₩", "").replace(",", ""));

            if (price <= maxPrice) {
                String offerName = o.get("title").asText();
                String offerUrl = o.get("url").asText();
                AttractionOffer offer = AttractionOffer.builder()
                        .offerName(offerName)
                        .offerPrice(price)
                        .offerUrl(offerUrl)
                        .build();
                offerList.add(offer);
            }
        }
        return offerList;
    }
}
