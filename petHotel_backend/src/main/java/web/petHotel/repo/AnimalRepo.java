package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.AnimalNameWithType;
import web.petHotel.entities.Animal;

public interface AnimalRepo extends ReactiveCrudRepository<Animal, Long>{

    Flux<Animal> findAnimalByOwner(String owner);

    Mono<Animal> findAnimalById(Long id);

    @Query("SELECT name, type_name, owner FROM core.animals\n" +
            "JOIN core.animal_types ON animal_type = core.animal_types.id\n" +
            "WHERE core.animals.id = :id")
    Mono<AnimalNameWithType> findAnimalNameWithTypeById(Long id);

}
