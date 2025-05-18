package pl.com.foks.myexpensesbackend.users.domain;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
