package com.milkcow.tripai.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.plan.dto.AccommodationData;
import com.milkcow.tripai.plan.dto.AccommodationDataDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanResult;
import java.util.ArrayList;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccommodationPlanServiceImpl implements AccommodationPlanService {

    @Value("${API-keys.Booking.SearchHotelDestination-URL}")
    private String SEARCH_DESTINATION_URL;
    @Value("${API-keys.Booking.SearchHotels-URL}")
    private String SEARCH_HOTELS_URL;
    @Value("${API-keys.Booking.API-Key}")
    private String APIKEY;
    @Value("${API-keys.Booking.API-Host}")
    private String APIHOST;

    @Override
    public AccommodationDataDto getAccommodationData(String destination, String startDate, String endDate,
                                                     int maxPrice) {
        try {
            ArrayList<AccommodationData> accommodationDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = setHeaders();

            String searchDestinationUrl = setDestinationURL(destination);

            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> destinationResponse = restTemplate.exchange(searchDestinationUrl, HttpMethod.GET, requestEntity,
                    String.class);
            if (destinationResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanResult.ACCOMMODATION_DESTINATION_API_REQUEST_FAILED);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode destinationJson = objectMapper.readTree(destinationResponse.getBody());
            long destId = destinationJson.path("data").path(0).get("dest_id").asLong();

            String hotelURL = setHotelURL(destId, startDate, endDate, maxPrice);

            ResponseEntity<String> hotelResponse = restTemplate.exchange(hotelURL, HttpMethod.GET, requestEntity,
                    String.class);
            if (destinationResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanResult.ACCOMMODATION_SEARCH_API_REQUEST_FAILED);
            }
            JsonNode accommodationJson = objectMapper.readTree(hotelResponse.getBody());
            if(!accommodationJson.get("status").asBoolean()){
                throw new PlanException(PlanResult.ACCOMMODATION_DESTINATION_API_REQUEST_FAILED);
            }
            JsonNode accommodationList = accommodationJson.path("data").path("hotels");

            for(JsonNode acc : accommodationList){
                AccommodationData accommodationData = parseAccommodationData(acc, startDate, endDate);
                accommodationDataList.add(accommodationData);
            }

            return AccommodationDataDto.builder()
                    .AccommodationCount(accommodationDataList.size())
                    .accommodationDataList(accommodationDataList)
                    .build();

        } catch (JsonProcessingException e) {
            throw new PlanException(PlanResult.ACCOMMODATION_API_RESPONSE_INVALID);
        }
    }

    private AccommodationData parseAccommodationData(JsonNode accommodation, String startDate, String endDate) {
        String name = accommodation.path("property").get("name").asText();
        double lat = accommodation.path("property").get("latitude").asDouble();
        double lng = accommodation.path("property").get("longitude").asDouble();
        double price = accommodation.path("property").path("priceBreakdown").path("grossPrice").get("value")
                .asDouble();
        String image = accommodation.path("property").path("photoUrls").path(0).asText();

        return AccommodationData.builder()
                .name(name)
                .lat(lat)
                .lng(lng)
                .startDate(startDate)
                .endDate(endDate)
                .price(price)
                .image(image)
                .build();
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", this.APIKEY);
        headers.add("X-RapidAPI-Host", this.APIHOST);

        return headers;
    }

    private String setDestinationURL(String destination) {
        return SEARCH_DESTINATION_URL + "?query=" + destination;
    }

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
