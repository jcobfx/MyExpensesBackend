package pl.com.foks.myexpensesbackend.config;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log
@Profile("local")
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.severe("An error occurred: " + e.getMessage());
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }
}
