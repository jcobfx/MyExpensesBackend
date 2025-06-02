package pl.com.foks.myexpensesbackend.roles.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.foks.myexpensesbackend.users.domain.User;

import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Role {
    @Id
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
