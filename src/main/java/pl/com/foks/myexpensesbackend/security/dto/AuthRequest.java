package pl.com.foks.myexpensesbackend.security.dto;

public record AuthRequest(String username, String password, String email) {
}
