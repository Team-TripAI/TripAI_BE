package com.milkcow.tripai.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.dto.FlightData;
import com.milkcow.tripai.plan.dto.FlightDataDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanResult;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightPlanServiceImpl implements FlightPlanService {

    @Value("${API-keys.Booking-X-API-URL}")
    private String BASE_URL;
    @Value("${API-keys.Booking-X-API-Key}")
    private String APIKEY;
    @Value("${API-keys.Booking-X-API-Host}")
    private String APIHOST;

    @Override
    public FlightDataDto getFlightData(String departureAirport, String arrivalAirport, String departureDate,
                                       int maxFare) {
        try {
            ArrayList<FlightData> flightDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            // Request 헤더 설정
            HttpHeaders headers = setHeader();

            // payload 설정
            String url = setURL(departureAirport, arrivalAirport, departureDate);

            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                if (response.getStatusCode().is4xxClientError()) {
                    throw new PlanException(PlanResult.API_KEY_LIMIT_EXCESS);
                } else {
                    throw new PlanException(PlanResult.FLIGHT_API_REQUEST_FAILED);
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseBody = objectMapper.readTree(response.getBody());

            JsonNode flightListJson = responseBody.path("data").path("flightOffers");
            for (JsonNode flight : flightListJson) {
                FlightData flightData = parseFlightData(departureAirport, arrivalAirport, departureDate, flight);

                if (flightData.getFare() <= maxFare) {
                    flightDataList.add(flightData);
                }
            }

            return FlightDataDto.builder()
                    .flightCount(flightDataList.size())
                    .flightDataList(flightDataList)
                    .build();
        } catch (JsonProcessingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        }
    }

    private FlightData parseFlightData(String departureAirport, String arrivalAirport, String departureDate,
                                       JsonNode flight) {
        JsonNode flightInfo = flight.path("segments").path(0).path("legs").path(0).get("flightInfo");
        int flightNumber = flightInfo.get("flightNumber").asInt();
        String airline = flightInfo.path("carrierInfo").get("operatingCarrier").asText();
        String flightId = airline + flightNumber;

        String[] departureDateTime = flight.get("segments").path(0).get("departureTime").asText().split("T");
        String[] arrivalDateTime = flight.get("segments").path(0).get("arrivalTime").asText().split("T");
        String departureTime = departureDateTime[1];
        String arrivalDate = arrivalDateTime[0];
        String arrivalTime = arrivalDateTime[1];

        int fare = flight.path("priceBreakdown").path("total").get("units").asInt();

        return FlightData.builder()
                .id(flightId)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureDate(departureDate)
                .departureTime(departureTime)
                .arrivalDate(arrivalDate)
                .arrivalTime(arrivalTime)
                .airline(airline)
                .fare(fare)
                .build();
    }

    private String setURL(String departureAirport, String arrivalAirport, String departureDate) {
        String url = this.BASE_URL
                + "?fromId=" + URLEncoder.encode(departureAirport + ".AIRPORT", StandardCharsets.UTF_8)
                + "&toId=" + URLEncoder.encode(arrivalAirport + ".AIRPORT", StandardCharsets.UTF_8)
                + "&departDate=" + URLEncoder.encode(departureDate, StandardCharsets.UTF_8)
                + "&pageNo=" + "1"
                + "&adults=" + "1"
                + "&sort=" + "BEST"
                + "&cabinClass=" + "ECONOMY"
                + "&currencyCode=" + "KRW";

        return url;
    }

    private HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", this.APIKEY);
        headers.add("X-RapidAPI-Host", this.APIHOST);

        return headers;
    }
}
