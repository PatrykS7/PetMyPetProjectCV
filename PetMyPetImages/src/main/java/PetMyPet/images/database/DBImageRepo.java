package PetMyPet.images.database;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface DBImageRepo extends ReactiveCrudRepository<DBImage, Long> {

    @Query("SELECT path FROM core.images WHERE hotel_id = :id")
    Flux<String> getPathByHotelId(Long id);

}
