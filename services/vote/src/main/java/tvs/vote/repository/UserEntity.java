package tvs.vote.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    @Column("id")
    private long id;

    @Column("email")
    private String email;
}
