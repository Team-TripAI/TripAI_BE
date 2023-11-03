package com.milkcow.tripai.plan.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DateValidatorTest {
    @Test
    public void 날짜양식_일치() throws Exception{
        //given
        String date = "2023-12-25";
        //when
        boolean validDate = DateValidator.isValidDate(date);
        //then
        assertThat(validDate).isTrue();
    }

    @Test
    public void 날짜양식_불일치() throws Exception{
        //given
        String date = "20231225";
        //when
        boolean validDate = DateValidator.isValidDate(date);
        //then
        assertThat(validDate).isFalse();
    }

}