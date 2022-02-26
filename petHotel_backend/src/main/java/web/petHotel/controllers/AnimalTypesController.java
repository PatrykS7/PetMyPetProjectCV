package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import web.petHotel.entities.AnimalTypes;
import web.petHotel.service.AnimalTypesService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnimalTypesController {

    @Autowired
    AnimalTypesService animalTypesService;

    @GetMapping("/getAllTypes")
    public Mono<Map<Long,String>> getAllTypes(){

        return animalTypesService.getAllTypes();
    }

    @GetMapping("/getTypeById/{id}")
    public Mono<AnimalTypes> getTypeById(@PathVariable Long id){

        return animalTypesService.getTypeById(id);
    }
}
