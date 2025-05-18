package pl.com.foks.myexpensesbackend.roles.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.foks.myexpensesbackend.roles.app.RoleService;
import pl.com.foks.myexpensesbackend.roles.dto.RoleModifyRequest;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @PutMapping("/add")
    public ResponseEntity<Void> addRole(@RequestBody RoleModifyRequest roleModifyRequest) {
        roleService.addRole(roleModifyRequest.role(), roleModifyRequest.userUuid());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeRole(@RequestBody RoleModifyRequest roleModifyRequest) {
        roleService.removeRole(roleModifyRequest.role(), roleModifyRequest.userUuid());
        return ResponseEntity.ok().build();
    }
}
