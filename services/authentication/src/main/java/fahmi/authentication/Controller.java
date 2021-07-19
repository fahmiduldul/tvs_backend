package fahmi.authentication;

import fahmi.authentication.request.CreateUserRequest;
import fahmi.authentication.security.JwtTool;
import fahmi.authentication.service.UserServiceImpl;
import fahmi.authentication.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/")
public class Controller {
    private UserServiceImpl userService;
    private ModelMapper mapper;
    private JwtTool jwtTool;

    @Autowired
    public Controller(UserServiceImpl userService, ModelMapper mapper, JwtTool jwtTool) {
        this.mapper = mapper;
        this.userService = userService;
        this.jwtTool = jwtTool;
    }

    @GetMapping("/status")
    public ResponseEntity<Mono<String>> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body(Mono.just("I'm On!"));
    }

    @PostMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){

        log.info("user request {}", createUserRequest);
        UserDTO userDTO = this.mapper.map(createUserRequest, UserDTO.class);
        Mono<UserDTO> userDTOMono = this.userService.createUser(userDTO);

        String token = this.jwtTool.encode(userDTO.getEmail());

        HttpCookie cookie = ResponseCookie.from("jwt",token).build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userDTOMono);
    }

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
}
