package pl.com.foks.myexpensesbackend.stripe.infrastructure;

import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StripeExceptionHandler {
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<String> handleStripeException(StripeException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body("Stripe error: " + e.getMessage());
    }
}
