package fahmi.authentication.service;

import fahmi.authentication.data.UserEntity;
import fahmi.authentication.data.UserRepository;
import fahmi.authentication.shared.UserDTO;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private PasswordEncoder passwordEncoder;
    private ModelMapper mapper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, ModelMapper mapper, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        UserEntity users = this.mapper.map(userDTO, UserEntity.class);
        users.setEncryptedPassword(this.passwordEncoder.encode(userDTO.getPassword()));

        Mono<UserEntity> returnValue = this.userRepository.save(users);

        return returnValue.map(user -> this.mapper.map(user, UserDTO.class));
    }

    @SneakyThrows
    @Override
    public Mono<UserDetails> findByUsername(String email) {
        Mono<UserEntity> userEntityMono = this.userRepository.findByEmail(email).log();

        userEntityMono.subscribe(user -> log.info("{}", user));
        return userEntityMono
                //.switchIfEmpty(Mono.just("this is empty").map(a -> {log.info("{}", a); return null;}))
                .map(userMono -> {
                    return new User(userMono.getEmail(), userMono.getEncryptedPassword(),
                            true, true, true, true,
                            new ArrayList<>()
                    );
                });
    }
}
