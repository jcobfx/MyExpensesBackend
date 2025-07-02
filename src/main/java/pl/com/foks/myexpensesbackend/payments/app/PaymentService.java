package pl.com.foks.myexpensesbackend.payments.app;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.foks.myexpensesbackend.payments.domain.Payment;
import pl.com.foks.myexpensesbackend.payments.domain.PaymentRepository;
import pl.com.foks.myexpensesbackend.payments.dto.PaymentRequest;
import pl.com.foks.myexpensesbackend.payments.dto.PaymentResponse;
import pl.com.foks.myexpensesbackend.payments.infrastructure.CustomerUtil;
import pl.com.foks.myexpensesbackend.roles.app.RoleService;
import pl.com.foks.myexpensesbackend.users.app.UserService;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CustomerUtil customerUtil;
    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final RoleService roleService;

    @Value("${stripe.keys.api-secret}")
    private String stripeApiSecret;

    @Value("${stripe.keys.api-public}")
    @Getter
    private String stripeApiPublic;

    @Value("${stripe.keys.webhook-secret}")
    private String stripeWebhookSecret;

    @Value("${stripe.api-version}")
    private String stripeApiVersion;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiSecret;
    }

    @Transactional(readOnly = true)
    public List<Payment> getPayments(String username) {
        return paymentRepository.findByUser(userService.findByUsername(username));
    }

    @Transactional
    public PaymentResponse initiatePayment(String username, String email, PaymentRequest paymentRequest) throws StripeException {
        Customer customer = customerUtil.findOrCreateCustomer(email, username);

        EphemeralKeyCreateParams ephemeralKeyCreateParams = EphemeralKeyCreateParams.builder()
                .setCustomer(customer.getId())
                .setStripeVersion(stripeApiVersion)
                .build();
        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyCreateParams);

        Price price = Price.retrieve(Product.retrieve(paymentRequest.productId()).getDefaultPrice());

        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
                .setAmount(price.getUnitAmount())
                .setCurrency(price.getCurrency())
                .setCustomer(customer.getId())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .putMetadata("user_uuid", userService.findByUsername(username).getUuid())
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);

        return new PaymentResponse(
                paymentIntent.getClientSecret(),
                ephemeralKey.getSecret(),
                customer.getId()
        );
    }

    @Transactional
    public void handleWebhook(String signature, String payload) {
        Stripe.apiKey = stripeApiSecret;
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);
        } catch (Exception e) {
            throw new RuntimeException("Invalid webhook signature", e);
        }
        switch (event.getType()) {
            case "payment_intent.created" -> {
                StripeObject data = event.getDataObjectDeserializer().getObject().orElseThrow();
                PaymentIntent paymentIntent = (PaymentIntent) data;
                String paymentIntentId = paymentIntent.getId();
                String userUuid = paymentIntent.getMetadata().get("user_uuid");
                if (paymentIntentId != null && userUuid != null && !userUuid.isBlank()) {
                    LocalDateTime now = LocalDateTime.now();
                    Payment payment = Payment.builder()
                            .user(userService.findByUuid(userUuid))
                            .paymentIntentId(paymentIntentId)
                            .status(Payment.Status.PENDING)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    paymentRepository.save(payment);
                }
            }
            case "payment_intent.succeeded" -> {
                StripeObject data = event.getDataObjectDeserializer().getObject().orElseThrow();
                String paymentIntentId = ((PaymentIntent) data).getId();
                if (paymentIntentId != null) {
                    paymentRepository.findByPaymentIntentId(paymentIntentId)
                            .ifPresent(payment -> {
                                payment.setStatus(Payment.Status.SUCCEEDED);
                                payment.setUpdatedAt(LocalDateTime.now());
                                paymentRepository.save(payment);

                                User user = userService.findByUsername(payment.getUser().getUsername());
                                user.getRoles().add(roleService.findByName("ROLE_PREMIUM"));
                                userService.save(user);
                            });
                }
            }
            case "payment_intent.payment_failed" -> {
                StripeObject data = event.getDataObjectDeserializer().getObject().orElseThrow();
                String paymentIntentId = ((PaymentIntent) data).getId();
                if (paymentIntentId != null) {
                    paymentRepository.findByPaymentIntentId(paymentIntentId)
                            .ifPresent(payment -> {
                                payment.setStatus(Payment.Status.FAILED);
                                payment.setUpdatedAt(LocalDateTime.now());
                                paymentRepository.save(payment);
                                // TODO: Handle failed payment, e.g., notify user
                            });
                }
            }
            case null, default -> {
            }
            // Handle other event types if necessary
        }
    }
}
