package com.milkcow.tripai.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.milkcow.tripai.global.exception.GeneralException;
import com.milkcow.tripai.global.result.ApiResult;
import com.milkcow.tripai.plan.dto.FlightData;
import com.milkcow.tripai.plan.dto.FlightDataDto;
import com.milkcow.tripai.plan.exception.PlanException;
import com.milkcow.tripai.plan.result.PlanResult;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FlightPlanService {
    private static final String url = "https://airline-api.naver.com/graphql";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * getFlightData 네이버 항공권 api를 사용하여 왕복 항공권 조회
     * @param departureAirport (출발지 공항)
     * @param arrivalAirport (도착지 공항)
     * @param departureDate (출발일)
     * @return @see FlightDataDto
     */
    public FlightDataDto getFlightData(String departureAirport, String arrivalAirport, String departureDate, int maxFare){
        try{
            ArrayList<FlightData> flightDataList = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();

            // Request 헤더 설정
            HttpHeaders headers = setHeader(departureAirport, arrivalAirport, departureDate);

            // 첫번째 payload설정
            JSONObject firstPayload = setPayload(departureAirport, arrivalAirport, departureDate, "", "");

            //requestEntity생성
            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(firstPayload, headers);

            // 첫번째 POST요청
            ResponseEntity<JSONObject> firstResponse = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
            if (firstResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanResult.FLIGHT_API_REQUEST_FAILED);
            }

            // 두번째 요청을 수행하기 위한 0.5초 휴식
            Thread.sleep(500);

            // 첫번째 JSON반환 처리
            JSONObject firstResponseJson = firstResponse.getBody();
            String galileoKey = getGalileoKeyFromJson(firstResponseJson.toJSONString());
            String travelBizKey = getTravelBizKeyFromJson(firstResponseJson.toJSONString());

            JSONObject secondPayload = setPayload(departureAirport, arrivalAirport, departureDate, galileoKey, travelBizKey);

            // 두번째 requestEntity생성
            HttpEntity<JSONObject> secondRequestEntity = new HttpEntity<>(secondPayload, headers);

            // 두번째 POST요청
            ResponseEntity<String> secondResponse = restTemplate.exchange(url, HttpMethod.POST, secondRequestEntity, String.class);

            if (secondResponse.getStatusCode() != HttpStatus.OK) {
                throw new PlanException(PlanResult.FLIGHT_API_REQUEST_FAILED);
            }

            String secondResponseBody = secondResponse.getBody();
            JsonNode jsonNode = objectMapper.readTree(secondResponseBody);

            JsonNode jsonResults = jsonNode.path("data").path("internationalList").get("results");
            if (jsonResults.get("schedules").isEmpty()){
                throw new PlanException(PlanResult.FLIGHT_API_RESPONSE_EMPTY);
            }

            JsonNode schedules = jsonResults.path("schedules").get(0);
            JsonNode fares = jsonResults.path("fares");

            if (schedules.size() == fares.size()) {
                for (JsonNode scheduleItem : schedules) {
                    FlightData flightData = parseFlightData(departureAirport, arrivalAirport, departureDate, fares, scheduleItem);
                    if(flightData.getFare() <= maxFare)
                        flightDataList.add(flightData);
                }
            }
            else{
                throw new PlanException(PlanResult.FLIGHT_API_RESPONSE_INVALID);
            }

            return FlightDataDto.builder()
                    .flightCount(flightDataList.size())
                    .flightDataList(flightDataList)
                    .build();

        }catch (JsonProcessingException | InterruptedException e){
            throw new GeneralException(ApiResult.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * parseFlightData JSON으로부터 개별 항공권에 대한 정보 추출
     * @param departureAirport (출발지 공항)
     * @param arrivalAirport (도착지 공항)
     * @param departureDate (출발일)
     * @param fares (전체 가격 정보 JSON)
     * @param scheduleItem (전체 항공권 정보 JSON)
     * @return @see FlightData
     */
    private static FlightData parseFlightData(String departureAirport, String arrivalAirport, String departureDate, JsonNode fares, JsonNode scheduleItem) {
        String key = scheduleItem.get("id").asText();
        int fareSum = 0;
        JsonNode fareItems = fares.get(key).get("fare").get("A01").get(0).get("Adult");
        for (JsonNode fare : fareItems) {
            fareSum += fare.asInt();
        }

        String airline = scheduleItem.get("detail").get(0).get("av").asText();
        String departureTime = scheduleItem.get("detail").get(0).get("sdt").asText().substring(8);
        String arrivalTime = scheduleItem.get("detail").get(0).get("edt").asText().substring(8);
        return FlightData.builder()
                .id(key)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .departureDate(departureDate)
                .airline(airline)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .fare(fareSum)
                .build();
    }

    /**
     * setPayloadHeader API요청을 위한 헤더 설정
     * @param departureAirport (출발지 공항)
     * @param arrivalAirport (도착지 공항)
     * @param departureDate (출발일)
     * @return HttpHeaders (HTTP Header)
     */
    private static HttpHeaders setHeader(String departureAirport, String arrivalAirport, String departureDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.add("Content-Length", "1237");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "NNB=VLR5WESSPQGGI; NaverSuggestUse=unuse%26unuse; ASID=0e3f022600000186f517c1a00000004f; NDARK=N; _ga_K2ECMCJBFQ=GS1.1.1682860344.4.0.1682860351.0.0.0; _ga_SQ24F7Q7YW=GS1.1.1682860345.4.0.1682860351.0.0.0; NID_AUT=UzIruiM2+XOWEgpP8cWhtWXf1kx9HkgZJDGFwM+XpagtHDSMGvxpZv92j4ErSsue; NID_JKL=7SY5BYjkGfZagfgwDjhVBYmk+JQT9D96P6ABvYcXNU4=; _ga_YTT2922408=GS1.1.1686813881.80.0.1686813881.0.0.0; NV_WETR_LOCATION_RGN_M=\"MDk2ODAxMTI=\"; NV_WETR_LAST_ACCESS_RGN_M=\"MDk2ODAxMTI=\"; nx_ssl=2; _ga=GA1.2.713034617.1678596531; page_uid=iMwRuwp0JXosskNosSCssssstN4-390944; NID_SES=AAABpPwyzscjqtYyxtFwueSAYXyFqvPrXTWi9a2KtXQSYa7y50S48auDOpBUuWpmzx/vHhuoxd1Mitqx12C8CNllHztXyHHaEkGOO3zzWuZ+SW69zS8h4Y1eJox991uZBBbDiUSqYXr9oO49ey948BeIN+/7AMTeLhIPbmZBfbOjshjY+wKUHU1R9IlQFFf6gzq4E5dYpp9jf1USV5/GRRZ71G7EFqGPflf1zHDZqtlYrBaM4faaQoHUtzuuvQIzM8pA+lqPzdbUAWEIhDhORU5ibgg7szmdDq7FY0wSCfRDex5QM/+EIyyQaolP41hqxB4rg1s+bpQSlTnEnzfIajpm2jH64PwN0YfijYcnIM8y3Z4OudrHryqe9F1V/xFroSYiINX/mBRBndG3T+RZwRFyoicuICGgrDbjYbdAV7mzjSYc3AAhej+p7sOzgkUlx+JMAwTEWOi5kekbsizZMB0FUkrCp1mrAYMhkjxsMtnQXbdrkAgfzCCRqUIcestmFvZ8MxpIhSOhMjq21IiLhS53X09ocCTEH2/Z3Dzf0XJyKOe2pfbZ/rS8dKXgYg2leC694w==");
        headers.setOrigin(url);
        headers.add("Referer", "https://flight.naver.com/flights/international/"+ departureAirport +"-"+ arrivalAirport +"-"+ departureDate +"?adult=1&isDirect=true&fareType=Y");
        headers.add("Sec-Ch-Ua", "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"");
        headers.add("Sec-Ch-Ua-Mobile", "?0");
        headers.add("Sec-Ch-Ua-Platform", "\"Windows\"");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Site", "same-site");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
        return headers;
    }

    /**
     * getPayload API요청을 위한 payload 설정
     * @param departureAirport (출발지 공항)
     * @param arrivalAirport (도착지 공항)
     * @param departureDate (출발일)
     * @param galileoKey (galileoKey)
     * @param travelBizKey (travelBizKey)
     * @return @see JSONObject (net.minidev.json.JSONObject)
     */
    private static JSONObject setPayload(String departureAirport, String arrivalAirport, String departureDate, String galileoKey, String travelBizKey) {
        JSONObject payload = new JSONObject();
        payload.put("operationName", "getInternationalList");

        JSONObject variables = new JSONObject();

        variables.put("adult", 1);
        variables.put("child", 0);
        variables.put("infant", 0);
        variables.put("where", "pc");
        variables.put("isDirect", true);
        variables.put("galileoFlag", true);
        variables.put("travelBizFlag", true);
        variables.put("fareType", "Y");

        ArrayNode itineraryArray = objectMapper.createArrayNode();
        ObjectNode itinerary = objectMapper.createObjectNode();
        itinerary.put("departureAirport", departureAirport);
        itinerary.put("arrivalAirport", arrivalAirport);
        itinerary.put("departureDate", departureDate);
        itineraryArray.add(itinerary);
        variables.put("itinerary", itineraryArray);

        variables.put("stayLength", "");
        variables.put("trip", "OW");
        variables.put("galileoKey", galileoKey);
        variables.put("travelBizKey", travelBizKey);

        payload.put("variables", variables);

        String query = "query getInternationalList($trip: String!, $itinerary: [InternationalList_itinerary]!, " +
                "$adult: Int = 1, $child: Int = 0, $infant: Int = 0, $fareType: String!, $where: String = \"pc\", " +
                "$isDirect: Boolean = true, $stayLength: String, $galileoKey: String, $galileoFlag: Boolean = true, " +
                "$travelBizKey: String, $travelBizFlag: Boolean = true) {\n" +
                "  internationalList(\n" +
                "    input: {trip: $trip, itinerary: $itinerary, person: {adult: $adult, child: $child, infant: $infant}, " +
                "fareType: $fareType, where: $where, isDirect: $isDirect, stayLength: $stayLength, galileoKey: $galileoKey, " +
                "galileoFlag: $galileoFlag, travelBizKey: $travelBizKey, travelBizFlag: $travelBizFlag}\n" +
                "  ) {\n" +
                "    galileoKey\n" +
                "    galileoFlag\n" +
                "    travelBizKey\n" +
                "    travelBizFlag\n" +
                "    totalResCnt\n" +
                "    resCnt\n" +
                "    results {\n" +
                "      airlines\n" +
                "      airports\n" +
                "      fareTypes\n" +
                "      schedules\n" +
                "      fares\n" +
                "      errors\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        payload.put("query", query);
        return payload;
    }

    /**
     * getGalileoKeyFromJson 첫번째 응답으로 부터 galileoKey 추출
     * @param firstResponseJson (첫번째 응답)
     * @return String (galileoKey)
     * @throws JsonProcessingException
     */
    private static String getGalileoKeyFromJson(String firstResponseJson) throws JsonProcessingException {
        return objectMapper.readTree(firstResponseJson).get("data").get("internationalList").get("galileoKey").asText();
    }

    /**
     * getTravelBizKeyFromJson 첫번째 응답으로 부터 travelBizKey 추출
     * @param firstResponseJson (첫번째 응답)
     * @return String (travelBizKey)
     * @throws JsonProcessingException
     */
    private static String getTravelBizKeyFromJson(String firstResponseJson) throws JsonProcessingException {
        return objectMapper.readTree(firstResponseJson).get("data").get("internationalList").get("travelBizKey").asText();
    }
}
