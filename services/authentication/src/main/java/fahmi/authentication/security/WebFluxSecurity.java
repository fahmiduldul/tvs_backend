package fahmi.authentication.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
public class WebFluxSecurity {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http){
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/**").permitAll()

                .and().formLogin()
                .authenticationFailureHandler((exchange, exception) -> Mono.error(exception))
                .authenticationSuccessHandler(new WebFilterChainServerAuthenticationSuccessHandler())

                .and().httpBasic();

        return http.build();
    }

    /**
     * MapReactiveUserDetailService is basically a ReactiveUserDetailService that store the data in Map
     * it's good for prototyping purpose
     * this basically is UserDetailsService for reactive application
     * @return
     */
    //@Bean
    //public MapReactiveUserDetailsService userDetailsService(){
    //    // adding default user to MapReactiveserDetailsService
    //    UserDetails user = User
    //            .withUsername("user")
    //            .password(this.passwordEncoder.encode("password"))
    //            .roles("USER")
    //            .build();
    //
    //    return new MapReactiveUserDetailsService(user);
    //}
}
