package com.milkcow.tripai.plan.service.flight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.dto.flight.FlightSearchData;
import com.milkcow.tripai.plan.dto.flight.FlightSearchResponseDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanSearchResult;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
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
 * 항공 서비스 구현
 */
@Service
public class FlightServiceImpl implements FlightService {

    @Value("${API-keys.Booking.SearchFlights-URL}")
    private String BASE_URL;
    @Value("${API-keys.Booking.API-Key}")
    private String APIKEY;
    @Value("${API-keys.Booking.API-Host}")
    private String APIHOST;

    private static final String BOOKING_POSTFIX = ".AIRPORT";

    @Override
    public FlightSearchResponseDto getFlightData(String departureAirport, String arrivalAirport, String departureDate,
                                                 int maxFare) {
        try {
            ArrayList<FlightSearchData> flightSearchDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            // Request 헤더 설정
            HttpHeaders headers = setHeader();

            // payload 설정
            String url = setURL(departureAirport + BOOKING_POSTFIX,
                    arrivalAirport + BOOKING_POSTFIX,
                    departureDate);

            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanSearchResult.FLIGHT_API_REQUEST_FAILED);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseBody = objectMapper.readTree(response.getBody());

            if (!responseBody.get("status").asBoolean()) {
                throw new PlanException(PlanSearchResult.FLIGHT_API_REQUEST_FAILED);
            }

            JsonNode flightListJson = responseBody.path("data").path("flightOffers");

            for (JsonNode flight : flightListJson) {
                Optional<FlightSearchData> flightData = parseFlightData(flight, departureAirport, arrivalAirport,
                        departureDate,
                        maxFare);

                flightData.ifPresent(flightSearchDataList::add);
            }

            return FlightSearchResponseDto.builder()
                    .flightCount(flightSearchDataList.size())
                    .flightSearchDataList(flightSearchDataList)
                    .build();
        } catch (JsonProcessingException e) {
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            throw new PlanException(PlanSearchResult.FLIGHT_API_KEY_LIMIT_EXCESS);
        }
    }

    /**
     * 항공 정보 Json을 객체로 파싱
     *
     * @param flight           Json
     * @param departureAirport 출발 공항 명(IATA 코드)
     * @param arrivalAirport   도착 공항 명(IATA 코드)
     * @param departureDate    출발 일자(yyyy-MM-dd 형식)명
     * @param maxFare          최대 항공비
     * @return {@link FlightSearchData}
     */
    private static Optional<FlightSearchData> parseFlightData(JsonNode flight, String departureAirport,
                                                              String arrivalAirport,
                                                              String departureDate,
                                                              int maxFare) {
        int fare = flight.path("priceBreakdown").path("total").get("units").asInt();

        if (fare > maxFare) {
            return Optional.empty();
        }

        JsonNode schedule = flight.path("segments").path(0);
        String airline = schedule.get("legs").get(0).get("flightInfo").get("carrierInfo").get("operatingCarrier")
                .asText();
        String flightNumber = schedule.get("legs").get(0).get("flightInfo").get("flightNumber").asText();
        String flightId = airline + flightNumber;

        String[] departureDateTime = schedule.get("departureTime").asText().split("T");
        String departureTime = departureDateTime[1];
        String[] arrivalDateTime = schedule.get("arrivalTime").asText().split("T");
        String arrivalDate = arrivalDateTime[0];
        String arrivalTime = arrivalDateTime[1];

        FlightSearchData flightSearchData = FlightSearchData.builder()
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
        return Optional.of(flightSearchData);
    }

    /**
     * 헤더 설정
     *
     * @return {@link HttpHeaders}
     */
    private HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", this.APIKEY);
        headers.add("X-RapidAPI-Host", this.APIHOST);

        return headers;
    }

    /**
     * 항공권 정보를 얻기 위한 API URL  설정
     *
     * @param departureAirport 출발 공항 명(IATA 코드)
     * @param arrivalAirport   도착 공항 명(IATA 코드)
     * @param departureDate    출발 일자(yyyy-MM-dd 형식)명
     * @return 항공권 정보 AIP URL
     */
    private String setURL(String departureAirport, String arrivalAirport, String departureDate) {
        String url = this.BASE_URL
                + "?fromId=" + URLEncoder.encode(departureAirport, StandardCharsets.UTF_8)
                + "&toId=" + URLEncoder.encode(arrivalAirport, StandardCharsets.UTF_8)
                + "&departDate=" + URLEncoder.encode(departureDate, StandardCharsets.UTF_8)
                + "&pageNo=1"
                + "&adults=1"
                + "&sort=BEST"
                + "&cabinClass=ECONOMY"
                + "&currency_code=" + "KRW";

        return url;
    }

}
