package fahmi.authentication;

import fahmi.authentication.data.UserEntity;
import fahmi.authentication.request.CreateUserRequest;
import fahmi.authentication.security.JwtUtil;
import fahmi.authentication.service.UserServiceImpl;
import fahmi.authentication.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/")
public class Controller {
    private UserServiceImpl userService;
    private ModelMapper mapper;
    private JwtUtil jwtUtil;

    private Environment env;

    @Autowired
    public Controller(UserServiceImpl userService, ModelMapper mapper,
                      JwtUtil jwtUtil, Environment env) {
        this.mapper = mapper;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.env = env;
    }

    @GetMapping("/status")
    public ResponseEntity<Mono<String>> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body(Mono.just("I'm On! The secret: " + env.getProperty("jwt.secret")));
    }

    @PostMapping(
            path = "/user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){

        log.info("user request {}", createUserRequest);
        UserDTO userDTO = this.mapper.map(createUserRequest, UserDTO.class);
        Mono<UserDTO> userDTOMono = this.userService.createUser(userDTO);

        String token = this.jwtUtil.encode(userDTO.getEmail());

        HttpCookie cookie = ResponseCookie.from("jwt",token).build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userDTOMono);
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(ServerWebExchange exchange){
        return exchange.getFormData()
                .map(x-> {
                    String username = x.get("username").get(0);
                    return this.jwtUtil.encode(username);
                })
                .map(token -> {
                    HttpCookie cookie = ResponseCookie.from("jwt",token).build();
                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, cookie.toString())
                            .body("logged in");
                });

        // THIS SHIT WAS TO VALUABLE TO BE DELETED
        // how to change DataBuffer to String
        //exchange.getRequest().getBody()
        //        .map(dataBuffer -> {
        //            byte[] bytes = new byte[dataBuffer.readableByteCount()];
        //            dataBuffer.read(bytes);
        //            DataBufferUtils.release(dataBuffer);
        //            return new String(bytes, StandardCharsets.UTF_8);
        //        })
        //        .collectList()
        //        .log()
        //        .subscribe();
    }

    @GetMapping("/token/validate/{token}")
    public Mono<Boolean> validateToken(@PathVariable String token){
        return Mono.just(token)
                .map(tok -> this.jwtUtil.validate(tok));
    }

    @GetMapping(path = "/user/entity/{email}")
    public Mono<ResponseEntity<UserEntity>> getUser(@PathVariable String email){
        return this.userService.findUserEntityByEmail(email)
                .map(userEntity -> ResponseEntity.ok().body(userEntity));
    }
}
