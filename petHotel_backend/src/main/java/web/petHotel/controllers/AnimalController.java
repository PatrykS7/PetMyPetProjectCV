package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Animal;
import web.petHotel.service.AnimalService;

@RestController
@RequestMapping("/api")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/saveAnimal")
    public Mono<Animal> saveAnimal(@RequestBody Mono<Animal> animalMono){

        return animalService.saveAnimal(animalMono);
    }

    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/animalByOwner/{owner}")
    public Flux<Animal> getAnimalByOwner(@PathVariable String owner){

        return animalService.getAnimalByOwner(owner);
    }

//    @GetMapping("/animals")
//    public Flux<Animal> getAnimals(){
//
//        return animalService.getAnimals();
//    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/patchAnimal")
    public Mono<Animal> patchAnimal(@RequestBody Mono<Animal> animalMono){

        return animalService.patchAnimal(animalMono);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteAnimal/{id}")
    public Mono<Void> deleteAnimal(@PathVariable Long id){

        return animalService.deleteAnimal(id);
    }
}
