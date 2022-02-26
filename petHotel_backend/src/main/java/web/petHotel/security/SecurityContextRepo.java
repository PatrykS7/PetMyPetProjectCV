package web.petHotel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepo implements ServerSecurityContextRepository {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        //System.out.println(exchange.getRequest().getCookies());

        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("PetMyPetJWT"))
                .flatMap(authHeader -> {

                    String token = (authHeader == null) ? null : authHeader.getValue();

                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
                    return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
                });
    }
}
