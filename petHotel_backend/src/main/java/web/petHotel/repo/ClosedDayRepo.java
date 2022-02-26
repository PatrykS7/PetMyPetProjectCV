package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import web.petHotel.entities.ClosedDay;

public interface ClosedDayRepo extends ReactiveCrudRepository<ClosedDay, Long> {

    @Query("select core.closed_days.id,hotel_id,animal_type,starting_date,ending_date from core.closed_days " +
            "JOIN core.hotels ON core.closed_days.hotel_id = core.hotels.id " +
            "where core.hotels.owner=:username")
    Flux<ClosedDay> getClosedDayByOwner(String username);

    Flux<ClosedDay> findClosedDayByHotelId(Long id);
}
