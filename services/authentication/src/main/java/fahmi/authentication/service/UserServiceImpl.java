package fahmi.authentication.service;

import fahmi.authentication.data.Users;
import fahmi.authentication.data.UserRepository;
import fahmi.authentication.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    private BCryptPasswordEncoder passwordEncoder;
    private ModelMapper mapper;
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, ModelMapper mapper, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        Users users = this.mapper.map(userDTO, Users.class);
        users.setEncryptedPassword(this.passwordEncoder.encode(userDTO.getPassword()));

        Mono<Users> returnValue = this.userRepository.save(users);

        return returnValue.map(user -> this.mapper.map(user, UserDTO.class));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    public Mono<UserDTO> loadUserByEmail(String email){
        Mono<Users> userEntity = this.userRepository.findByEmail(email);

        return userEntity
                .map(user -> this.mapper.map(user, UserDTO.class));
    }
}
