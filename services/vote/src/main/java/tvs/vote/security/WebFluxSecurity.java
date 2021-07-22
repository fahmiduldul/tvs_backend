package tvs.vote.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebFluxSecurity {
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http){
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/**").permitAll();

        return http.build();
    }
}
