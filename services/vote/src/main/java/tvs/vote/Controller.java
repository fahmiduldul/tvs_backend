package tvs.vote;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Controller {
    @GetMapping("/status/check")
    public Mono<String> statusCheck(){
        return Mono.just("I'm ON!");
    }

    @PostMapping("/vote/{poll}/{choice}")
    public Mono<ResponseEntity> vote(ServerHttpRequest req, @PathVariable String poll, @PathVariable String choice){
        req.getHeaders().get(HttpHeaders.AUTHORIZATION);
        return null;
    }
}
