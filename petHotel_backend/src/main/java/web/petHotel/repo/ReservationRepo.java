package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import web.petHotel.entities.Hotel;
import web.petHotel.entities.Reservation;

public interface ReservationRepo extends ReactiveCrudRepository<Reservation, Long> {


    @Query("SELECT * FROM core.reservations " +
            "JOIN core.animals on core.reservations.animal_id = core.animals.id " +
            "WHERE core.animals.owner = :username AND core.reservations.status='W'")
    Flux<Reservation> getReservationByUsername(String username);

    @Query("SELECT * FROM core.reservations " +
            "JOIN core.hotels on core.reservations.hotel_id = core.hotels.id " +
            "WHERE core.hotels.owner = :owner AND core.reservations.status='W'")
    Flux<Reservation> getOwnerReservations(String owner);

    @Query("SELECT * FROM core.reservations " +
            "JOIN core.animals on core.reservations.animal_id = core.animals.id " +
            "WHERE core.animals.owner = :username AND core.reservations.status!='W'")
    Flux<Reservation> getFinishedReservationByUsername(String username);

    @Query("SELECT * FROM core.reservations " +
            "JOIN core.hotels on core.reservations.hotel_id = core.hotels.id " +
            "WHERE core.hotels.owner = :owner AND core.reservations.status!='W'")
    Flux<Reservation> getFinishedOwnerReservations(String owner);



}
