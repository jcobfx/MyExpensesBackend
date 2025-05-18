package pl.com.foks.myexpensesbackend.financial_records.domain;

public class FinancialRecordNotFoundException extends RuntimeException {
    public FinancialRecordNotFoundException(String message) {
        super(message);
    }
}
