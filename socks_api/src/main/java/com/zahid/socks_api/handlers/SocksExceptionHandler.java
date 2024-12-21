package com.zahid.socks_api.handlers;

import com.zahid.socks_api.SocksApiApplication;
import com.zahid.socks_api.exceptions.LackOfSocksException;
import com.zahid.socks_api.exceptions.SocksDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SocksExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleException(LackOfSocksException e) {
        SocksApiApplication.logger.error("Response has not sent because of following error: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(SocksDataException e) {
        SocksApiApplication.logger.error("Response has not sent because of following error: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException e) {
        SocksApiApplication.logger.error("Response has not sent because of following error: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
