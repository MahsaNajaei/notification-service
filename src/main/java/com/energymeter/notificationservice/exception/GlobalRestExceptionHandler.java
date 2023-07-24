package com.energymeter.notificationservice.exception;

import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleAllExceptions(Exception e) {
        log.error("Unexpected Exception Occurred! ", e);
        return generateBaseApiErrorMessage("Sorry! Some Internal Error Occurred! Please Try Again Later!");
    }

    @ExceptionHandler(ShutdownSignalException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleApplicationRuntimeExceptions(ShutdownSignalException e) {
        log.warn(e.getMessage());
        return generateBaseApiErrorMessage("No notification is Available!");
    }

    private Map<String, String> generateBaseApiErrorMessage(String message) {
        var responseBody = new HashMap<String, String>();
        responseBody.put("message", message);
        return responseBody;
    }
}
