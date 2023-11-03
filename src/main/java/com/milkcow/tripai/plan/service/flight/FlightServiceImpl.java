package com.milkcow.tripai.plan.service.flight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.dto.FlightData;
import com.milkcow.tripai.plan.dto.FlightDataDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanResult;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

@Service
public class FlightServiceImpl implements FlightService {

    @Value("${API-keys.TripAdvisor.SearchFlights-URL}")
    private String BASE_URL;
    @Value("${API-keys.TripAdvisor.API-Key}")
    private String APIKEY;
    @Value("${API-keys.TripAdvisor.API-Host}")
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
                throw new PlanException(PlanResult.FLIGHT_API_REQUEST_FAILED);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseBody = objectMapper.readTree(response.getBody());

            JsonNode flightListJson = responseBody.path("data").path("flights");

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
        } catch (JsonProcessingException | MalformedURLException | UnsupportedEncodingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        }catch (HttpClientErrorException e){
            throw new PlanException(PlanResult.FLIGHT_API_KEY_LIMIT_EXCESS);
        }
    }

    private static FlightData parseFlightData(String departureAirport, String arrivalAirport, String departureDate,
                                              JsonNode flight) {
        int fare = flight.path("purchaseLinks").path(0).get("totalPrice").asInt();
        String url = flight.path("purchaseLinks").path(0).get("url").asText();
        JsonNode schedule = flight.path("segments").path(0).path("legs").path(0);
        String airline = schedule.get("marketingCarrierCode").asText();
        String flightNumber = schedule.get("flightNumber").asText();
        String flightId = airline + flightNumber;

        String[] departureDateTime = schedule.get("departureDateTime").asText().split("T");
        String departureTime = departureDateTime[1];
        String[] arrivalDateTime = schedule.get("arrivalDateTime").asText().split("T");
        String arrivalDate = arrivalDateTime[0];
        String arrivalTime = arrivalDateTime[1];

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
                .url(url)
                .build();
    }

    private HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", this.APIKEY);
        headers.add("X-RapidAPI-Host", this.APIHOST);

        return headers;
    }

    private String setURL(String departureAirport, String arrivalAirport, String departureDate)
            throws UnsupportedEncodingException, MalformedURLException {
        String url = this.BASE_URL
                + "?sourceAirportCode=" + URLEncoder.encode(departureAirport, StandardCharsets.UTF_8)
                + "&destinationAirportCode=" + URLEncoder.encode(arrivalAirport, StandardCharsets.UTF_8)
                + "&date=" + URLEncoder.encode(departureDate, StandardCharsets.UTF_8)
                + "&itineraryType=" + "ONE_WAY"
                + "&sortOrder=" + "PRICE"
                + "&numAdults=" + "1"
                + "&numSeniors=" + "0"
                + "&classOfService=" + "ECONOMY"
                + "&pageNumber=" + "1"
                + "&nonstop=" + "yes"
                + "&currencyCode=" + "KRW";

        return url;
    }

}
