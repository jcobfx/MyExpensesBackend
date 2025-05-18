package pl.com.foks.myexpensesbackend.security.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.com.foks.myexpensesbackend.users.domain.User;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token.expiration}")
    private long expiration;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(secret));
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 60 * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        claims.put("roles", user.getRoles());
        return Jwts.builder()
                .header().type("JWT")
                .and()
                .subject(user.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey())
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String tUsername = extractUsername(token);
        return (tUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
