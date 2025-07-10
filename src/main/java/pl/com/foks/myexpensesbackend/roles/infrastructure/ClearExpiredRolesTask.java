package pl.com.foks.myexpensesbackend.roles.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.com.foks.myexpensesbackend.roles.app.RoleService;

@Component
@RequiredArgsConstructor
public class ClearExpiredRolesTask {
    private final RoleService roleService;

    @Scheduled(cron = "@daily")
    @Transactional
    public void clearExpiredPremiumRoles() {
        roleService.clearExpiredPremiumRoles();
    }
}
