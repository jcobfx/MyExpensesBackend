package pl.com.foks.myexpensesbackend.financial_records.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.com.foks.myexpensesbackend.financial_records.app.FinancialRecordService;
import pl.com.foks.myexpensesbackend.financial_records.domain.Category;
import pl.com.foks.myexpensesbackend.financial_records.domain.CategoryRepository;
import pl.com.foks.myexpensesbackend.financial_records.domain.FinancialRecord;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/financial-records")
@RequiredArgsConstructor
public class FinancialRecordController {
    private final FinancialRecordService financialRecordService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping
    public ResponseEntity<Set<FinancialRecord>> getFinancialRecords(@AuthenticationPrincipal User user) {
        Set<FinancialRecord> financialRecords = financialRecordService.getFinancialRecords(user);
        return ResponseEntity.ok(financialRecords);
    }

    @PutMapping
    public ResponseEntity<FinancialRecord> createFinancialRecord(@AuthenticationPrincipal User user,
                                                                 @RequestBody FinancialRecord financialRecord) {
        if (financialRecord.getCategory() == null) {
            return ResponseEntity.badRequest().body(financialRecord);
        }
        financialRecord.setUser(user);
        FinancialRecord createdFinancialRecord = financialRecordService.createFinancialRecord(financialRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFinancialRecord);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteFinancialRecord(@AuthenticationPrincipal User user, @PathVariable String uuid) {
        financialRecordService.deleteFinancialRecord(user, uuid);
        return ResponseEntity.noContent().build();
    }
}
