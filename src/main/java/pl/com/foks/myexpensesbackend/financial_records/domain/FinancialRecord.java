package pl.com.foks.myexpensesbackend.financial_records.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FinancialRecord {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(unique = true, nullable = false)
    private String uuid;
}
