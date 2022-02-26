package web.petHotel.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import web.petHotel.entities.User;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    //TODO move to application.properties
    private final String secret = "############################################################";
    private final Long expirationTime = 43200L;

    private final Key key;
    private final JwtParser parser;

    public JWTUtil() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateToken(User usr) {

        Date currDate = new Date();

        return Jwts.builder()
                .setSubject(usr.getUsername())
                .addClaims( Map.of("role",usr.getAuthority()))
                .setIssuedAt(currDate)
                .setExpiration(new Date(currDate.getTime() + expirationTime * 1000))
                .signWith(key)
                .compact();
    }

    public Claims getClaimsFromToken(String token){

        return parser.parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token){

        try {
            return getClaimsFromToken(token).getSubject();
        }
        catch (Exception e) {
            return null;
        }

    }

    public String getAuthorityFromToken(String token){

        return getClaimsFromToken(token).get("role", String.class);
    }

    public boolean isValid(String token, String username){

        Claims claims = getClaimsFromToken(token);
        boolean exp = claims.getExpiration().after(new Date()); //check if token expired

        return exp && !username.isEmpty();
    }
}
