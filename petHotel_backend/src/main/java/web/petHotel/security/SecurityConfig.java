package web.petHotel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    SecurityContextRepo securityContextRepo;

    @Bean
    SecurityWebFilterChain webFilterChain(ServerHttpSecurity http){

         final String[] AUTH_WHITELIST = {
                 //our
                 "/api/test",
                 "/api/login",
                 "/api/mobileLogin",
                 "/api/registerUser",
                 "/api/registerOwner",
                 "/api/hotels",
                 "/api/hotel/**",
                 "/api/getAllTypes",
                 "/api/getTypeById/**",
                 "/api/closedDaysByHotelId/**",
                 "/api/hotelsFilter",
                // -- Swagger UI v2
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                // -- Swagger UI v3 (OpenAPI)
                "/v3/api-docs/**",
                "/swagger-ui/**"
        };

        return http
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                }))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }))
                .and()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepo)
                .authorizeExchange()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}