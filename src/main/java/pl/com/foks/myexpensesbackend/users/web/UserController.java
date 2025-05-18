package pl.com.foks.myexpensesbackend.users.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.foks.myexpensesbackend.users.app.UserService;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getUuid());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user, @PathVariable String uuid) {
        if (!Objects.equals(user.getUuid(), uuid) && userService.isUserAdmin(uuid)) {
            throw new IllegalArgumentException("You cannot delete an admin account.");
        }
        userService.deleteUser(uuid);
        return ResponseEntity.ok().build();
    }
}
