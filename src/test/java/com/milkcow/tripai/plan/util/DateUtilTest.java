package com.milkcow.tripai.plan.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateUtilTest {
    @Test
    public void 날짜양식_일치() throws Exception{
        //given
        String date = "2023-12-25";
        //when
        boolean validDate = DateUtil.isValidDate(date);
        //then
        assertThat(validDate).isTrue();
    }

    @Test
    public void 날짜양식_불일치() throws Exception{
        //given
        String date = "20231225";
        //when
        boolean validDate = DateUtil.isValidDate(date);
        //then
        assertThat(validDate).isFalse();
    }

    @Test
    public void 기간계산() throws Exception{
        //given
        String start = "2023-12-25";
        String end = "2023-12-26";
        //when
        long duration = DateUtil.calculateDuration(start, end);
        //then
        assertThat(duration).isEqualTo(1);
    }

}