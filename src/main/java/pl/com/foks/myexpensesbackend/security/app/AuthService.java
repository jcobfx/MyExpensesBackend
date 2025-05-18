package pl.com.foks.myexpensesbackend.security.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.foks.myexpensesbackend.security.domain.RefreshToken;
import pl.com.foks.myexpensesbackend.security.domain.UserAlreadyExistsException;
import pl.com.foks.myexpensesbackend.security.dto.AuthResponse;
import pl.com.foks.myexpensesbackend.security.infrastructure.JwtUtil;
import pl.com.foks.myexpensesbackend.users.app.UserService;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse login(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        if (auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            String token = jwtUtil.generateToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(username).getToken();
            return new AuthResponse(token, refreshToken);
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Transactional
    public User register(String username, String password, String email) {
        if (userService.existsByUsernameOrEmail(username, email)) {
            throw new UserAlreadyExistsException("User with this username or email already exists");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createdAt(LocalDateTime.now())
                .active(true)
                .uuid(UUID.randomUUID().toString())
                .build();
        return userService.save(user);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String username = user.getUsername();
                    String token = jwtUtil.generateToken(user);
                    String refToken = refreshTokenService.createRefreshToken(username).getToken();
                    return new AuthResponse(token, refToken);
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }
}
