package fahmi.authentication;

import fahmi.authentication.request.CreateUserRequest;
import fahmi.authentication.request.LoginRequest;
import fahmi.authentication.service.UserService;
import fahmi.authentication.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/")
public class Controller {
    private ReactiveUserDetailsService userService;
    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public Controller(ReactiveUserDetailsService userService, ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/status")
    public ResponseEntity<Mono<String>> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body(Mono.just("I'm On!"));
    }

//    @PostMapping(
//            path = "/user",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Mono<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
//
//        log.info("user request {}", createUserRequest);
//        UserDTO userDTO = this.mapper.map(createUserRequest, UserDTO.class);
//        Mono<UserDTO> userDTOMono = this.userService.createUser(userDTO);
//
////        HttpCookie cookie = ResponseCookie.from("tvs","cookie").build();
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
////                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .body(userDTOMono);
//    }

    @PostMapping("/login")
    public ResponseEntity<Mono<String>> login(){

        return ResponseEntity.status(200).body(Mono.just("logged in!"));
    }

    @GetMapping(path = "/user/{email}")
    public ResponseEntity<Mono<UserDetails>> getUser(@PathVariable String email){
        Mono<UserDetails> userDTOMono = this.userService.findByUsername(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userDTOMono);
    }

    @PostMapping("/login1")
    public Mono<Boolean> checkLogin(@RequestBody LoginRequest request){
        Mono<UserDetails> user = this.userService.findByUsername(request.getEmail());

        return user.map(u -> {
            if(u.getUsername().equals(request.getEmail()) &&
                    this.passwordEncoder.encode(u.getPassword()).equals(request.getPassword())){
                log.info("true");
                return true;
            } else {
                log.info("false");
                return false;
            }
        });
    }
}
