package pl.com.foks.myexpensesbackend.roles.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.foks.myexpensesbackend.roles.domain.Role;
import pl.com.foks.myexpensesbackend.roles.domain.RoleNotFoundException;
import pl.com.foks.myexpensesbackend.roles.domain.RoleRepository;
import pl.com.foks.myexpensesbackend.users.app.UserService;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Transactional
    public void addRole(String role, String userUuid) {
        if (Objects.equals(role, "ROLE_ADMIN")) {
            throw new IllegalArgumentException("You cannot assign the ADMIN role to a user.");
        }
        User user = userService.findByUuid(userUuid);
        Role roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + role));
        user.getRoles().add(roleEntity);
        userService.save(user);
    }

    @Transactional
    public void removeRole(String role, String userUuid) {
        if (Objects.equals(role, "ROLE_ADMIN")) {
            throw new IllegalArgumentException("You cannot remove the ADMIN role from a user.");
        }
        User user = userService.findByUuid(userUuid);
        Role roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + role));
        user.getRoles().remove(roleEntity);
        userService.save(user);
    }
}
