package web.petHotel;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import web.petHotel.JSON.AuthResponse;
import web.petHotel.entities.User;
import web.petHotel.repo.ReviewsRepo;
import web.petHotel.security.JWTUtil;

import java.util.Date;

@SpringBootTest
class petHotelApplicationTests {

    @Autowired
    private ReviewsRepo reviewsRepo;

    @Test
    void contextLoads() {
    }

    @Test
    void tokenData(){

        JWTUtil jwt = new JWTUtil();
        User usr = new User("us_nm","Passy",true,"ROLE_USER");
        AuthResponse authResponse = new AuthResponse(jwt.generateToken(usr), usr.getAuthority());

        System.out.println( "Username: " + jwt.getUsernameFromToken( authResponse.getToken()));
    }

    @Test
    void isTokenValid(){

        JWTUtil jwt = new JWTUtil();
        User usr = new User("us_nm","Passy",true,"ROLE_USER");
        AuthResponse authResponse = new AuthResponse(jwt.generateToken(usr), usr.getAuthority());

        Claims claims = jwt.getClaimsFromToken(authResponse.getToken());
        System.out.println(claims);
        System.out.println(claims.getExpiration().after(new Date()));
    }

    @Test
    void getReviews(){

        reviewsRepo.getAll()
                .doOnNext( s -> System.out.println(s))
                .switchIfEmpty( e -> System.out.println("empty"))
                .subscribe();
    }
}
