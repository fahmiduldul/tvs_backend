package tvs.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tvs.apigateway.configuration.Configuration;
import tvs.apigateway.utils.JwtTool;

@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter {
    private final JwtTool jwtTool;

    @Autowired
    public AuthenticationFilter(JwtTool jwtTool) {
        this.jwtTool = jwtTool;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest req = exchange.getRequest();
        ServerHttpResponse res = exchange.getResponse();

        // check if path is public
        if(Configuration.publicPath.contains(req.getPath().toString())){
            log.info("public path called");
            return chain.filter(exchange);
        }

        // check if available
        if (!req.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return this.rejectRequest(res);
        }

        // check if token valid
        if (!this.jwtTool.validate(req.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0))) {
            return this.rejectRequest(res);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> rejectRequest(ServerHttpResponse res){
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        return res.setComplete();
    }

}
