package fahmi.authentication.service;

import fahmi.authentication.data.UserEntity;
import fahmi.authentication.data.UserRepository;
import fahmi.authentication.security.JwtUtil;
import fahmi.authentication.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
    private JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(
            PasswordEncoder passwordEncoder, ModelMapper mapper,
            UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        UserEntity users = this.mapper.map(userDTO, UserEntity.class);
        users.setEncryptedPassword(this.passwordEncoder.encode(userDTO.getPassword()));

        Mono<UserEntity> returnValue = this.userRepository.save(users);

        return returnValue.map(user -> this.mapper.map(user, UserDTO.class));
    }

    @Override
    public Mono<Boolean> isTokenValid(String token) {
        return Mono.just(token)
                .map(tok -> this.jwtUtil.validate(tok));
    }

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        Mono<UserEntity> userEntityMono = this.userRepository.findByEmail(email);

        return userEntityMono
                .map(userMono -> new User(userMono.getEmail(), userMono.getEncryptedPassword(),
                        true, true, true, true,
                        new ArrayList<>()
                ));
    }

    public Mono<UserEntity> findUserEntityByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
}
