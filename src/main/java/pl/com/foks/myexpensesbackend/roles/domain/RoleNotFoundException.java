package pl.com.foks.myexpensesbackend.roles.domain;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
