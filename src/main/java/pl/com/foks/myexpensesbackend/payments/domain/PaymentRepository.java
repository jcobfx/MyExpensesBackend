package pl.com.foks.myexpensesbackend.payments.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
    List<Payment> findByUser(User user);
}
