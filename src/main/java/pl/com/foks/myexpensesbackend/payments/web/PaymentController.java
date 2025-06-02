package pl.com.foks.myexpensesbackend.payments.web;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.com.foks.myexpensesbackend.payments.app.PaymentService;
import pl.com.foks.myexpensesbackend.payments.domain.Payment;
import pl.com.foks.myexpensesbackend.payments.dto.PaymentRequest;
import pl.com.foks.myexpensesbackend.payments.dto.PaymentResponse;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getPayments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.getPayments(user.getUsername()));
    }

    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok(paymentService.getStripeApiPublic());
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<PaymentResponse> initiatePayment(@AuthenticationPrincipal User user,
                                                    @RequestBody PaymentRequest paymentRequest) throws StripeException {
        return ResponseEntity.ok(paymentService.initiatePayment(user.getUsername(), user.getEmail(), paymentRequest));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestHeader("Stripe-Signature") String signature,
                                                @RequestBody String payload) {
        paymentService.handleWebhook(signature, payload);
        return ResponseEntity.ok().build();
    }
}
