package pl.com.foks.myexpensesbackend.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    @Modifying
    @Query("delete from RefreshToken rt where rt.expiryDate < :date")
    void deleteAllByExpiryDateBefore(LocalDateTime date);
}
