package com.zahid.socks_api.handlers;

import com.zahid.socks_api.exceptions.LackOfSocksException;
import com.zahid.socks_api.exceptions.SocksDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SocksExceptionHandlerTests {
    private final SocksExceptionHandler handler = new SocksExceptionHandler();
    @Test
    public void handleException_LackOfSocksException_MethodNotAllowedStatus() {
        ResponseEntity<String> actualResponse =
                handler.handleException(new LackOfSocksException(1,1));
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, actualResponse.getStatusCode());
    }
    @Test
    public void handleException_SocksDataException_BadRequestStatus() {
        ResponseEntity<String> actualResponse =
                handler.handleException(new SocksDataException(""));
        assertEquals(HttpStatus.BAD_REQUEST, actualResponse.getStatusCode());
    }
}
