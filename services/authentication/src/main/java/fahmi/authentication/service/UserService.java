package fahmi.authentication.service;

import fahmi.authentication.shared.UserDTO;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService {
    Mono<UserDTO> createUser(UserDTO userDTO);
}
