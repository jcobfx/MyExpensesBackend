package pl.com.foks.myexpensesbackend.config;

import io.jsonwebtoken.JwtException;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.com.foks.myexpensesbackend.financial_records.domain.FinancialRecordNotFoundException;
import pl.com.foks.myexpensesbackend.roles.domain.RoleNotFoundException;
import pl.com.foks.myexpensesbackend.security.domain.UserAlreadyExistsException;
import pl.com.foks.myexpensesbackend.users.domain.UserNotFoundException;

@ControllerAdvice
@Log
@Profile("local")
public class GlobalExceptionHandler {
    @ExceptionHandler({AuthenticationException.class, JwtException.class})
    public ResponseEntity<String> handleAuthenticationException(Exception e) {
        log.severe("Authentication error: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication error: " + e.getMessage());
    }

    @ExceptionHandler(exception = {
            FinancialRecordNotFoundException.class,
            RoleNotFoundException.class,
            UserNotFoundException.class,})
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        log.severe("Resource not found: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found: " + e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.severe("User already exists: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.severe("An error occurred: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}
