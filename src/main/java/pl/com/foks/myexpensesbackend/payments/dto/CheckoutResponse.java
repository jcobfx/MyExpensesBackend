package pl.com.foks.myexpensesbackend.payments.dto;

public record CheckoutResponse(String paymentIntent, String ephemeralKey, String customerId, String stripePublicKey) {
}
