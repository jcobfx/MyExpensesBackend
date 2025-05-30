package pl.com.foks.myexpensesbackend.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndActiveTrue(String username);
    Optional<User> findByUuid(String uuid);
    boolean existsByUsernameOrEmail(String username, String email);
}
