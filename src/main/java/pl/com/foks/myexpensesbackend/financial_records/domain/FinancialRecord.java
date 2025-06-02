package pl.com.foks.myexpensesbackend.financial_records.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String title;
    private int value;
    private String category;
    private LocalDateTime createdAt;
    private String uuid;
}
