package com.milkcow.tripai.plan.embedded;

import lombok.Getter;

/**
 * 식당의 요일별 영업시간
 */
@Getter
public class RestaurantHour {

    private final DayOfWeek day;
    private final String open;
    private final String close;

    public RestaurantHour(DayOfWeek day, String open, String close) {
        this.day = day;
        this.open = open;
        this.close = close;
    }

    /**
     * 요일, 시간에 따른 RestaurantHour 반환
     * @param dayOfWeek {@link DayOfWeek} 요일에 따른 번호
     * @param openTotal 영업 시작 시간(분 단위)
     * @param closeTotal 영업 종료 시간(분 단위)
     * @return {@link RestaurantHour}
     */
    public static RestaurantHour of(int dayOfWeek, int openTotal, int closeTotal) {
        int openHour = openTotal / 60;
        int openMinute = openTotal % 60;
        int closeHour = closeTotal / 60;
        int closeMinute = closeTotal % 60;

        String open = openHour + ":" + openMinute;
        String close = closeHour + ":" + closeMinute;
        DayOfWeek day = DayOfWeek.of(dayOfWeek);

        return new RestaurantHour(day, open, close);
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
