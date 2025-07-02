package pl.com.foks.myexpensesbackend.security.infrastructure;

import io.jsonwebtoken.JwtException;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.com.foks.myexpensesbackend.security.domain.InvalidRefreshTokenException;
import pl.com.foks.myexpensesbackend.security.domain.RefreshTokenExpiredException;

@ControllerAdvice
@Log
public class SecurityExceptionHandler {
    @ExceptionHandler({RefreshTokenExpiredException.class, InvalidRefreshTokenException.class,
            AuthenticationException.class, JwtException.class})
    public ResponseEntity<String> handleRefreshTokenException(Exception e) {
        log.severe("Security error: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
