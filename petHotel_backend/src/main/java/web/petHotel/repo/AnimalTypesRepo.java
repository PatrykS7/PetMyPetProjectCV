package web.petHotel.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import web.petHotel.entities.AnimalTypes;

public interface AnimalTypesRepo extends ReactiveCrudRepository<AnimalTypes, Long> {
}
