package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Animal;
import web.petHotel.repo.AnimalRepo;

@Service
public class AnimalService {

    private final AnimalRepo animalRepo;

    @Autowired
    public AnimalService(AnimalRepo animalRepo) {
        this.animalRepo = animalRepo;
    }

    public Mono<Animal> saveAnimal(Mono<Animal> animalMono) {

        return animalRepo.saveAll(animalMono).next()
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Can't save animal")));
    }

    public Flux<Animal> getAnimals() {

        return animalRepo.findAll();
    }

    public Mono<Animal> patchAnimal(Mono<Animal> animalMono) {

        return animalMono.map( m -> {
            m.setNew(false);
            return m;
        })
                .flatMap( j -> animalRepo.save(j));
    }

    public Flux<Animal> getAnimalByOwner(String owner) {

        return animalRepo.findAnimalByOwner(owner)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find any animals")));
    }

    public Mono<Void> deleteAnimal(Long id) {

        return animalRepo.deleteById(id);
    }
}
