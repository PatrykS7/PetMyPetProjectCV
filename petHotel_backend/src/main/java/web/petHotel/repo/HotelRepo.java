package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Hotel;


public interface HotelRepo extends ReactiveCrudRepository<Hotel, Long> {

    Flux<Hotel> findHotelByOwner(String owner);

    @Query("SELECT id,name,description,street,zipcode,city,phone_number,contact_email,owner FROM core.hotels " +
            "JOIN core.favourites ON core.favourites.hotel_id = core.hotels.id " +
            "AND core.favourites.username=:username")
    Flux<Hotel> selectFauvoritesHotelsByUsername(String username);

    Mono<Hotel> findHotelById(Long id);

    @Query("SELECT * FROM core.hotels LIMIT :limit OFFSET :omit")
    Flux<Hotel> findHotelByPages(int limit, int omit);

    @Query("SELECT name FROM core.hotels WHERE id = :id")
    Mono<String> findHotelNameById(Long id);

}
