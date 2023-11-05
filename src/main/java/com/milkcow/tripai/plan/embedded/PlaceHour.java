package com.milkcow.tripai.plan.embedded;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * 식당의 요일별 영업시간
 */
@Getter
public class PlaceHour {

    private final DayOfWeek day;
    private final String open;
    private final String close;

    public PlaceHour(DayOfWeek day, String open, String close) {
        this.day = day;
        this.open = open;
        this.close = close;
    }

    /**
     * 요일, 시간에 따른 RestaurantHour 반환
     * @param dayOfWeek {@link DayOfWeek} 요일에 따른 번호
     * @param openTotal 영업 시작 시간(분 단위)
     * @param closeTotal 영업 종료 시간(분 단위)
     * @return {@link PlaceHour}
     */
    public static PlaceHour of(int dayOfWeek, int openTotal, int closeTotal) {
        int openHour = openTotal / 60;
        int openMinute = openTotal % 60;
        int closeHour = closeTotal / 60;
        int closeMinute = closeTotal % 60;

        String open = openHour + ":" + openMinute;
        String close = closeHour + ":" + closeMinute;
        DayOfWeek day = DayOfWeek.of(dayOfWeek);

        return new PlaceHour(day, open, close);
    }

    /**
     * 맛집, 명소에서 영업시간 파싱 메서드
     * @param json results.data.[i] 형식
     * @return {@link PlaceHour}
     */
    public static List<PlaceHour> parseHoursList(JsonNode json) {
        ArrayList<PlaceHour> hourList = new ArrayList<>();
        if(json.has("hours")){
            JsonNode hourListJson = json.path("hours").get("week_ranges");
            for (int i = 0; i < hourListJson.size(); i++) {
                JsonNode h = hourListJson.get(i);

                if (h.size() > 0) {
                    int openTime = h.path(0).get("open_time").asInt();
                    int closeTime = h.path(0).get("close_time").asInt();
                    hourList.add(PlaceHour.of(i, openTime, closeTime));
                }
            }
        }
        return hourList;
    }

    @Override
    public String toString() {
        return "RestaurantHour{" +
                "day='" + day +
                ", open='" + open + '\'' +
                ", close='" + close + '\'' +
                '}';
    }
}
