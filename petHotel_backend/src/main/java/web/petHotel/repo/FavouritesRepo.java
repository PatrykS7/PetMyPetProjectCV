package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity  //UGH
class RootEntity {
    @Id
    private Long id;
}

public interface FavouritesRepo extends ReactiveCrudRepository<RootEntity,Long> {

    @Query("DELETE from core.favourites where username=:username AND hotel_id=:hotelId")
    Mono<Void> deleteFavouriteHotel(String username,Long hotelId);

    @Query("INSERT INTO core.favourites VALUES (:username,:hotelId)")
    Mono<Void> addFavouriteHotel(String username,Long hotelId);
}
