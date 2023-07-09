package com.mundim.WeekMethod.exception.config;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.exception.UnauthorizedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@ControllerAdvice
public class ApiExceptionHandler{

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {NullFieldException.class})
    public ResponseEntity<Object> handleNullFieldException(NullFieldException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {UnauthorizedRequestException.class})
    public ResponseEntity<Object> handleUnauthorizedRequestException(UnauthorizedRequestException e){
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                e.getMessage(),
                unauthorized,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, unauthorized);
    }
}