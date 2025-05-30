package pl.com.foks.myexpensesbackend.payments.infrastructure;

import com.stripe.exception.StripeException;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log
@Profile("local")
public class PaymentExceptionHandler {
    @ExceptionHandler(StripeException.class)
    public ResponseEntity<String> handleStripeException(StripeException e) {
        log.severe("Stripe error: " + e.getMessage());
        return ResponseEntity.status(e.getStatusCode())
                .body("Stripe error: " + e.getMessage());
    }
}
