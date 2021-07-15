package fahmi.authentication.data;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "users")
@Table(name="users")
public class Users {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", unique = true)
    private String email;

    @org.springframework.data.relational.core.mapping.Column("password")
    private String encryptedPassword;

    @Column(name = "name")
    private String name;
}
