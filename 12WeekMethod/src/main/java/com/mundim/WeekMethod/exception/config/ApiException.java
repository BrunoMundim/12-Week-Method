package com.mundim.WeekMethod.exception.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final String timeStamp;

}