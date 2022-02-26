package web.petHotel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import web.petHotel.JSON.AuthResponse;
import web.petHotel.entities.*;
import web.petHotel.security.JWTUtil;

@SpringBootTest
@AutoConfigureWebTestClient
public class GetEndpointsStatusTests {

    private String getUserToken(){

        JWTUtil jwt = new JWTUtil();
        User usr = new User("username","Passy",true,"ROLE_USER");
        AuthResponse authResponse = new AuthResponse(jwt.generateToken(usr), usr.getAuthority());
        return authResponse.getToken();
    }

    private String getOwnerToken(){

        JWTUtil jwt = new JWTUtil();
        User usr = new User("username","Passy",true,"ROLE_OWNER");
        AuthResponse authResponse = new AuthResponse(jwt.generateToken(usr), usr.getAuthority());
        return authResponse.getToken();
    }

    @Autowired
    WebTestClient webTestClient;

    @Test                                   //HOTELS
    void getAllHotelsTest(){

        webTestClient.get().uri("/api/hotels")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Hotel.class)
                .value(hotels ->
                        Assertions.assertTrue(hotels.size()>0));
    }

    @Test
    void getAllHotelByIdTest(){

        webTestClient.get().uri("/api/hotel/1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Hotel.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void getFavouriteHotelsTest(){

        webTestClient.get().uri("/api/favourites/user@dupa.com").cookie("PetMyPetJWT", getUserToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Hotel.class)
                .value(hotels ->
                        Assertions.assertTrue(hotels.size()>0));
    }

    @Test
    void getHotelsByOwnerTest(){

        webTestClient.get().uri("/api/hotelByOwner/owner@dupa.com").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Hotel.class)
                .value(hotels ->
                        Assertions.assertTrue(hotels.size()>0));
    }

    @Test                                   //PRICELIST
    void getPriceListTest(){

        webTestClient.get().uri("/api/getPriceListByHotelId/1").cookie("PetMyPetJWT", getUserToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PriceList.class)
                .value(pr ->
                        Assertions.assertTrue(pr.size()>0));
    }

    @Test                                   //RESERVATIONS
    void getReservationByIdTest(){

        webTestClient.get().uri("/api/reservation/1").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reservation.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void getUserReservationsTest(){

        webTestClient.get().uri("/api/userReservations/user@dupa.com").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Reservation.class)
                .value(res ->
                        Assertions.assertTrue(res.size()>0));
    }

    @Test
    void getOwnerReservationsTest(){

        webTestClient.get().uri("/api/ownerReservations/akebbellk@upenn.edu").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Reservation.class)
                .value(res ->
                        Assertions.assertTrue(res.size()>0));
    }

    @Test                                   //ACCOUNTDETAILS
    void getAccountDetailsByUsernameTest(){

        webTestClient.get().uri("/api/accountDetails/user@dupa.com").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountDetails.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void getOwnerAccountDetailsByUsernameTest(){

        webTestClient.get().uri("/api/ownerAccountDetails/owner@dupa.com").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountDetails.class)
                .value(Assertions::assertNotNull);
    }

    @Test                                   //REVIEWS
    void getAllReviewsTest(){

        webTestClient.get().uri("/api/reviews").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Reviews.class)
                .value(rev ->
                        Assertions.assertTrue(rev.size()>0));
    }

    @Test                                   //CLOSEDDAY
    void getClosedDayByHotelIdTest(){

        webTestClient.get().uri("/api/closedDaysByHotelId/1").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ClosedDay.class)
                .value(cd ->
                        Assertions.assertTrue(cd.size()>0));
    }

    @Test
    void getClosedDayByOwnerTest(){

        webTestClient.get().uri("/api/closedDaysByOwner/owner@dupa.com").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ClosedDay.class)
                .value(cd ->
                        Assertions.assertTrue(cd.size()>0));
    }

    @Test                                   //ANIMAL
    void getAnimalByOwnerTest(){

        webTestClient.get().uri("/api/animalByOwner/user@dupa.com").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Animal.class)
                .value(animals ->
                        Assertions.assertTrue(animals.size()>0));
    }

    @Test                                   //ANIMALTYPES
    void getAnimalTypesTest(){

        webTestClient.get().uri("/api/getAllTypes").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AnimalTypes.class)
                .value(at ->
                        Assertions.assertTrue(at.size()>0));
    }

    @Test
    void getAnimalTypesByIdTest(){

        webTestClient.get().uri("/api/getTypeById/1").cookie("PetMyPetJWT", getOwnerToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AnimalTypes.class)
                .value(Assertions::assertNotNull);
    }
}
