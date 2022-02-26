package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.AnimalTypes;
import web.petHotel.repo.AnimalTypesRepo;

import java.util.Map;

@Service
public class AnimalTypesService {

    private final AnimalTypesRepo animalTypesRepo;

    @Autowired
    public AnimalTypesService(AnimalTypesRepo animalTypesRepo) {
        this.animalTypesRepo = animalTypesRepo;
    }


    public Mono<Map<Long,String>> getAllTypes() {

        return animalTypesRepo.findAll().collectMap(AnimalTypes::getId, AnimalTypes::getTypeName);
    }

    public Mono<AnimalTypes> getTypeById(Long id) {

        return animalTypesRepo.findById(id);
    }
}
