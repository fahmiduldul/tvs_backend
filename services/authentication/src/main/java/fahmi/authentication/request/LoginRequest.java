package fahmi.authentication.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @Email(message = "Not a valid email address")
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;
}
