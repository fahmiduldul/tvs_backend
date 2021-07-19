package fahmi.authentication.data;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    @Column("id")
    private long id;

    @Column("email")
    private String email;

    @Column("password")
    private String encryptedPassword;

    @Column("name")
    private String name;
}
