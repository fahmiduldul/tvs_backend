package fahmi.authentication.service;

import fahmi.authentication.shared.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import reactor.core.publisher.Mono;

public interface UserService extends UserDetailsService {
    Mono<UserDTO> createUser(UserDTO userDTO);
}
