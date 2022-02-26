package web.petHotel.security;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JWTUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(authToken);

        if (username != null) {
            return Mono.just(jwtUtil.isValid(authToken, username))
                    .filter(valid -> valid)
                    .switchIfEmpty(Mono.empty())
                    .map(valid -> {

                        Claims claims = jwtUtil.getClaimsFromToken(authToken);
                        String role = jwtUtil.getAuthorityFromToken(authToken);

                        return new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );
                    });
        }
        else
            return Mono.empty();
    }
}