package com.advancia.chat4me_auth_service.application.exceptions;

import com.advancia.Chat4Me_Auth_Service.generated.application.model.ErrorMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Log4j2
@ControllerAdvice
public class ApiAuthExceptionHandler {
    private void logError(Exception ex, WebRequest request) {
        log.error(
            "An error happened while calling {} API: {}",
            ((ServletWebRequest) request).getRequest().getRequestURI(),
            ex.getMessage(), ex
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(exception.getClass().getName());
        errorMessage.setErrorDesc(exception.getMessage());
        logError(exception, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(exception.getClass().getName());
        errorMessage.setErrorDesc(exception.getMessage());
        logError(exception, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}