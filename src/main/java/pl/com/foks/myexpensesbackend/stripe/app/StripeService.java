package pl.com.foks.myexpensesbackend.stripe.app;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.foks.myexpensesbackend.stripe.dto.CheckoutRequest;
import pl.com.foks.myexpensesbackend.stripe.dto.CheckoutResponse;
import pl.com.foks.myexpensesbackend.stripe.infrastructure.CustomerUtil;

@Service
@RequiredArgsConstructor
public class StripeService {
    private final CustomerUtil customerUtil;

    @Value("${stripe.keys.api-secret}")
    private String stripeSecretKey;

    @Value("${stripe.keys.api-public}")
    private String stripePublicKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public CheckoutResponse checkout(String username, String email, CheckoutRequest checkoutRequest) throws StripeException {
        Customer customer = customerUtil.findOrCreateCustomer(email, username);

        EphemeralKeyCreateParams ephemeralKeyCreateParams = EphemeralKeyCreateParams.builder()
                .setCustomer(customer.getId())
                .setStripeVersion("2025-04-30.basil")
                .build();
        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyCreateParams);

        Price price = Price.retrieve(Product.retrieve(checkoutRequest.productId()).getDefaultPrice());

        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
                .setAmount(price.getUnitAmount())
                .setCurrency(price.getCurrency())
                .setCustomer(customer.getId())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);

        return new CheckoutResponse(
                paymentIntent.getClientSecret(),
                ephemeralKey.getSecret(),
                customer.getId(),
                stripePublicKey
        );
    }
}
