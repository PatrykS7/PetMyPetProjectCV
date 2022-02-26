package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.UsernameAndHotel;
import web.petHotel.service.FavouriteService;

@RestController
@RequestMapping("/api")
public class FavouriteController {

    @Autowired
    private FavouriteService favoutitesService;

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/deleteFavourite")
    public Mono<Void> deleteFavouriteHotel(@RequestBody UsernameAndHotel usernameAndHotel){

        return favoutitesService.deleteFavouriteHotel(usernameAndHotel);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addFavourite")
    public Mono<Void> addFavouriteHotel(@RequestBody UsernameAndHotel usernameAndHotel){

        return favoutitesService.addFavouriteHotel(usernameAndHotel);
    }
}
