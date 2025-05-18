package pl.com.foks.myexpensesbackend.security.dto;

public record AuthResponse(String token, String refreshToken) {
}
