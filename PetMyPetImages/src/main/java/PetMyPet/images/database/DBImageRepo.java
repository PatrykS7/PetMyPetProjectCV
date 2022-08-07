package PetMyPet.images.database;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DBImageRepo extends ReactiveCrudRepository<DBImage, Long> {

    @Query("SELECT path FROM core.images WHERE hotel_id = :id")
    Flux<String> getPathByHotelId(Long id);

    Mono<Void> deleteByPath(String path);

    @Query("UPDATE core.images SET main_image = null WHERE hotel_id = :id")
    Mono<Void> setMainImageNull(Long id);

    @Query("UPDATE core.images SET main_image = true WHERE path = :path")
    Mono<Void> setMainImage(String path);

    @Query("SELECT path FROM core.images WHERE hotel_id = :id AND main_image = true")
    Mono<String> getMainPathByHotelId(Long id);

}
