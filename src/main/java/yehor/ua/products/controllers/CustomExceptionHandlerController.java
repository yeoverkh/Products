package yehor.ua.products.controllers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandlerController implements AuthenticationEntryPoint {
    /**
     * This variable contain all values from application.properties.
     */
    private final Environment messages;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage = messages.getProperty("error.unauthorized");

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials() {
        String errorMessage = messages.getProperty("error.bad-credentials");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleUnprocessableJson() {
        String errorMessage = messages.getProperty("error.unprocessable-json");

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFilledJson() {
        String errorMessage = messages.getProperty("error.enter-data-is-empty");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleEntityExists() {
        String errorMessage = messages.getProperty("error.entity.already-exists");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound() {
        String errorMessage = messages.getProperty("error.entity.not-found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
