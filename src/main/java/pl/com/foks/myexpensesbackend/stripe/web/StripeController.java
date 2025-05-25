package pl.com.foks.myexpensesbackend.stripe.web;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.foks.myexpensesbackend.stripe.app.StripeService;
import pl.com.foks.myexpensesbackend.stripe.dto.CheckoutRequest;
import pl.com.foks.myexpensesbackend.stripe.dto.CheckoutResponse;
import pl.com.foks.myexpensesbackend.users.domain.User;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
public class StripeController {
    private final StripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@AuthenticationPrincipal User user,
                                                     @RequestBody CheckoutRequest checkoutRequest) throws StripeException {
        return ResponseEntity.ok(stripeService.checkout(user.getUsername(), user.getEmail(), checkoutRequest));
    }
}
