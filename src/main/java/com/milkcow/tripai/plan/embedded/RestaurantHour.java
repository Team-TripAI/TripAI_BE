package com.milkcow.tripai.plan.embedded;

import lombok.Getter;

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
