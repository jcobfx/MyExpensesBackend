package pl.com.foks.myexpensesbackend.payments.dto;

public record PaymentResponse(String clientSecret, String ephemeralKey, String customerId) {
}
