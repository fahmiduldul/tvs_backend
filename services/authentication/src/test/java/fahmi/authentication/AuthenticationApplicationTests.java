package fahmi.authentication;

import fahmi.authentication.data.UserEntity;
import fahmi.authentication.data.UserRepository;
import fahmi.authentication.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
@SpringBootTest
class AuthenticationApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        Mono<UserDetails> publisher = this.userService.findByUsername("afahmi13@live.com")
                .doOnNext(userDetails -> log.info("userDetails: {}", userDetails));

        publisher.subscribe();

        //Mono.just("asd").log().subscribe();
    }

    @Test
    void test2(){
        this.userRepository.findByEmail("example@email.com").log().subscribe();
    }

    @Test
    void test(){
        Flux<Integer> pub = Flux.range(0,6).log();
                //.doOnNext(a -> log.info("{}", a));

        pub.subscribe(i -> log.info("{}", i));
    }

}
