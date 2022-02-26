package web.petHotel.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Animal;

public interface AnimalRepo extends ReactiveCrudRepository<Animal, Long>{

    Flux<Animal> findAnimalByOwner(String owner);

    Mono<Animal> findAnimalById(Long id);

}
