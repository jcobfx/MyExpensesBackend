package pl.com.foks.myexpensesbackend.financial_records.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.Optional;
import java.util.Set;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    Set<FinancialRecord> findByUser(User user);
    Optional<FinancialRecord> findByUuidAndUser(String uuid, User user);
}
