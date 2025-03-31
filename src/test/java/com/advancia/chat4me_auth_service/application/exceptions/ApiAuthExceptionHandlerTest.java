package com.advancia.chat4me_auth_service.application.exceptions;

import com.advancia.Chat4Me_Auth_Service.generated.application.model.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ApiAuthExceptionHandlerTest {
    private ApiAuthExceptionHandler apiAuthExceptionHandler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        apiAuthExceptionHandler = new ApiAuthExceptionHandler();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/test-endpoint");
        mockRequest = new ServletWebRequest(servletRequest);
    }

    @Test
    void shouldThrowAnException_whenIsAllOk() {
        Exception exception = new Exception("Test exception");

        ResponseEntity<ErrorMessage> response = apiAuthExceptionHandler.handleException(exception, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test exception", response.getBody().getErrorDesc());
        assertEquals("java.lang.Exception", response.getBody().getErrorCode());
    }

    @Test
    void shouldThrowARuntimeException_whenIsAllOk() {
        RuntimeException runtimeException = new RuntimeException("Test runtime exception");

        ResponseEntity<ErrorMessage> response = apiAuthExceptionHandler.handleRuntimeException(runtimeException, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test runtime exception", response.getBody().getErrorDesc());
        assertEquals("java.lang.RuntimeException", response.getBody().getErrorCode());
    }
}