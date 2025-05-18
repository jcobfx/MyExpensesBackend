package pl.com.foks.myexpensesbackend.security.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.foks.myexpensesbackend.security.app.AuthService;
import pl.com.foks.myexpensesbackend.security.dto.AuthRequest;
import pl.com.foks.myexpensesbackend.security.dto.AuthResponse;
import pl.com.foks.myexpensesbackend.security.dto.RefreshRequest;
import pl.com.foks.myexpensesbackend.users.domain.User;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest.username(), authRequest.password()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        User registeredUser = authService.register(authRequest.username(), authRequest.password(), authRequest.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.getUuid());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        return ResponseEntity.ok(authService.refresh(refreshRequest.refreshToken()));
    }
}
