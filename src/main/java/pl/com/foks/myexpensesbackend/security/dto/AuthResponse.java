package pl.com.foks.myexpensesbackend.security.dto;

public record AuthResponse(String accessToken, String refreshToken, boolean isRefresh) {
}
