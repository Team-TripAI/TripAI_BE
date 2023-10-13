package com.milkcow.tripai.global.result;

import org.springframework.http.HttpStatus;

public interface ResultProvider {

    Integer getCode();

    HttpStatus getStatus();

    String getMessage();
}
