package com.milkcow.tripai.plan.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator {

    // yyyy-MM-dd 형식의 문자열인지 확인하는 정규표현식
    private static final String DATE_PATTERN = "^(\\d{4}-\\d{2}-\\d{2})$";

    public static boolean isValidDate(String dateStr) {
        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(dateStr);
        return matcher.matches();
    }
}