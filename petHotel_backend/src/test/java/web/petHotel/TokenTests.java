package web.petHotel;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import web.petHotel.JSON.AuthResponse;
import web.petHotel.entities.User;
import web.petHotel.security.JWTUtil;

import java.util.Date;

@SpringBootTest
class TokenTests {

    @Test
    void contextLoads() {
    }

    @Test
    void tokenData(){

        JWTUtil jwt = new JWTUtil();
        User usr = new User("username","Passy",true,"ROLE_USER");
        AuthResponse authResponse = new AuthResponse(jwt.generateToken(usr), usr.getAuthority());

        Assertions.assertEquals("username", jwt.getUsernameFromToken( authResponse.getToken()));
    }

    @Test
    void isTokenValid(){

        JWTUtil jwt = new JWTUtil();
        User usr = new User("username","Passy",true,"ROLE_USER");
        AuthResponse authResponse = new AuthResponse(jwt.generateToken(usr), usr.getAuthority());

        Claims claims = jwt.getClaimsFromToken(authResponse.getToken());

        Assertions.assertTrue(claims.getExpiration().after(new Date()));
        Assertions.assertEquals("username", claims.getSubject());
    }
}
