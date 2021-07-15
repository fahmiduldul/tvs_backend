package fahmi.authentication;

import fahmi.authentication.request.CreateUserRequest;
import fahmi.authentication.service.UserService;
import fahmi.authentication.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/")
public class Controller {
    private UserService userService;
    private ModelMapper mapper;

    @Autowired
    public Controller(UserService userService, ModelMapper mapper) {
        this.mapper = mapper;
        this.userService = userService;
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

//        HttpCookie cookie = ResponseCookie.from("tvs","cookie").build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userDTOMono);
    }

//    @GetMapping(path = "/user/{email}")
//    public ResponseEntity<Mono<UserDTO>> getUser(@PathVariable String email){
//        Mono<UserDTO> userDTOMono = this.userService.loadUs
//    }
}
