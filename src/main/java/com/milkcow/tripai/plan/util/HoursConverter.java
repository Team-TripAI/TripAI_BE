package com.milkcow.tripai.plan.util;

import com.milkcow.tripai.plan.embedded.PlaceHour;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeConverter;

/**
 * {@link PlaceHour} 리스트를 문자열로 DB에 저장
 */
public class HoursConverter implements AttributeConverter<List<PlaceHour>, String> {

    private static final String EMPTY = "null";

    /**
     * DB저장 시 문자열로 변환<p>
     * 형식: 요일번호-영업시작시간-영업종료시간,
     * @param attribute  변환될 엔티티 -
     *                   {@code List<PlaceHour>}
     * @return 문자열로 변환된 {@code List<PlaceHour>}
     */
    @Override
    public String convertToDatabaseColumn(List<PlaceHour> attribute) {
        if(attribute.isEmpty()){
            return EMPTY;
        }
        StringBuilder hoursDb = new StringBuilder();
        for (PlaceHour hour : attribute) {
            int day = hour.getDay().getDayNum();
            String open = hour.getOpen();
            String close = hour.getClose();

            hoursDb.append(day).append("-").append(open).append("-").append(close).append(",");
        }
        return hoursDb.toString();
    }

    /**
     * DB에서 가져온 문자열을 엔티티로 변환시 사용
     * @param dbData  변환될 컬럼
     * @return {@link PlaceHour}
     */
    @Override
    public List<PlaceHour> convertToEntityAttribute(String dbData) {
        if(dbData.equals(EMPTY)){
            return Collections.emptyList();
        }

        List<PlaceHour> placeHours = new ArrayList<>();
        String[] hoursString = dbData.split(",");
        for(String hour : hoursString){
            String[] placeHourString = hour.split("-");
            int day = Integer.parseInt(placeHourString[0]);
            String open = placeHourString[1];
            String close = placeHourString[2];

            placeHours.add(PlaceHour.of(day, open, close));
        }
        return placeHours;
    }
}
