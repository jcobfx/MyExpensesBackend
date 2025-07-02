package pl.com.foks.myexpensesbackend.security.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.foks.myexpensesbackend.security.domain.RefreshTokenRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CleanRefreshTokensTask {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "@weekly")
    @Transactional
    public void cleanRefreshTokens() {
        refreshTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now().minusDays(7));
    }

    @Scheduled(cron = "0 */2 * * * *")
    @Transactional
    @Profile("local")
    public void cleanRefreshTokensForLocal() {
        refreshTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now().minusMinutes(2));
    }
}
