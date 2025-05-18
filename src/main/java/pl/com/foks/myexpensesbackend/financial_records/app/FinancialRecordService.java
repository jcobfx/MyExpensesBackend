package pl.com.foks.myexpensesbackend.financial_records.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.foks.myexpensesbackend.financial_records.domain.FinancialRecord;
import pl.com.foks.myexpensesbackend.financial_records.domain.FinancialRecordNotFoundException;
import pl.com.foks.myexpensesbackend.financial_records.domain.FinancialRecordRepository;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {
    private final FinancialRecordRepository financialRecordRepository;

    @Transactional(readOnly = true)
    public Set<FinancialRecord> getFinancialRecords(User user) {
        return financialRecordRepository.findByUser(user);
    }

    @Transactional
    public FinancialRecord createFinancialRecord(FinancialRecord financialRecord) {
        financialRecord.setUuid(UUID.randomUUID().toString());
        financialRecord.setCreatedAt(LocalDateTime.now());
        return financialRecordRepository.save(financialRecord);
    }

    @Transactional
    public void deleteFinancialRecord(User user, String uuid) {
        FinancialRecord financialRecord = financialRecordRepository.findByUuidAndUser(uuid, user)
                .orElseThrow(() -> new FinancialRecordNotFoundException("Financial record not found"));
        financialRecordRepository.delete(financialRecord);
    }
}
