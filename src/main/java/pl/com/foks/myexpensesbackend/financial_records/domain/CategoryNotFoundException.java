package pl.com.foks.myexpensesbackend.financial_records.domain;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
