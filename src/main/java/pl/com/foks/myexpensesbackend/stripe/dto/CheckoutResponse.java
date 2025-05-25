package pl.com.foks.myexpensesbackend.stripe.dto;

public record CheckoutResponse(String paymentIntent, String ephemeralKey, String customerId, String stripePublicKey) {
}
