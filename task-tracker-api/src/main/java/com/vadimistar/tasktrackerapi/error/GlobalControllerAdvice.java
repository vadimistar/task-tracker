package com.vadimistar.tasktrackerapi.error;

import com.vadimistar.tasktrackerapi.security.auth.UserAlreadyExistsException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Configuration
@Log4j2
public class GlobalControllerAdvice {

    @Value("${task-tracker.log.max-stack.trace:10}")
    private long maxStackTrace;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ErrorDto errorDto = new ErrorDto(String.join(". ", errors));
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDto> handleInvalidRequestFormatExceptions(Exception e) {
        log.error("Invalid request format: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto("Invalid request format");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorDto errorDto = new ErrorDto("Invalid email or password");
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex) {
        String stackTrace = Arrays.stream(ex.getStackTrace())
                .limit(maxStackTrace)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
        log.error("Exception occurred {}: {}\nStack trace:\n{}",
                ex.getClass().toString(), ex.getMessage(), stackTrace);
        ErrorDto errorDto = new ErrorDto("Internal server error");
        return ResponseEntity.internalServerError().body(errorDto);
    }
}
