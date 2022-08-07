package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Hotel;

import java.time.LocalDate;


public interface HotelRepo extends ReactiveCrudRepository<Hotel, Long> {

    Flux<Hotel> findAllByOwner(String owner);

    @Query("UPDATE core.hotels SET geog = ST_SetSRID(ST_MakePoint(:lat, :lon), 4326) WHERE id = :id")
    Mono<Hotel> updateGeolocalization(Long id, Double lat, Double lon);

    @Query("SELECT id,name,description,street,zipcode,city,phone_number,contact_email,owner FROM core.hotels " +
            "JOIN core.favourites ON core.favourites.hotel_id = core.hotels.id " +
            "AND core.favourites.username=:username")
    Flux<Hotel> selectFauvoritesHotelsByUsername(String username);

    Mono<Hotel> findHotelById(Long id);

    @Query("SELECT * FROM core.hotels LIMIT :limit OFFSET :omit")
    Flux<Hotel> findHotelByPages(int limit, int omit);

    @Query("SELECT name FROM core.hotels WHERE id = :id")
    Mono<String> findHotelNameById(Long id);

    @Query("SELECT * " +
            "FROM core.hotels b JOIN core.price_list on b.id = core.price_list.hotel_id " +
            "WHERE :range >= (SELECT ST_DistanceSphere(ST_MakePoint( :lon, :lat), ST_MakePoint(b.lon,b.lat)))" +
            " AND NOT EXISTS ( " +
            "    SELECT core.closed_days.id,hotel_id,animal_type,starting_date,ending_date  " +
            "    FROM core.closed_days JOIN core.hotels ON core.closed_days.hotel_id = core.hotels.id " +
            "        WHERE core.hotels.owner=b.owner  " +
            "        AND animal_type = :animalType  " +
            "        AND :startingDate BETWEEN starting_date AND ending_date " +
            "        AND :endingDate BETWEEN starting_date AND ending_date " +
            "    ) AND core.price_list.animal_type = :animalType " +
            "LIMIT :limit OFFSET :omit")
    Flux<Hotel> findHotelsByFiltersByPages(Double lon, Double lat, Integer range, LocalDate startingDate, LocalDate endingDate, Long animalType, int limit, int omit);

    @Query("DELETE FROM core.hotels WHERE id = :hotelId")
    Mono<Void> deleteHotelById(Long hotelId);
}
