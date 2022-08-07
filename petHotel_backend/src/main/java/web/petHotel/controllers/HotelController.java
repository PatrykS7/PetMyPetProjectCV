package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.Filters;
import web.petHotel.entities.Hotel;
import web.petHotel.service.HotelService;

@RestController
@RequestMapping("/api")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/hotels")
    public Flux<Hotel> getHotelByPages(@RequestParam(value = "page", defaultValue = "1") int page){

        return hotelService.getHotelByPages(page);
    }

    @PostMapping("/hotelsFilter")
    public Flux<Hotel> getFilteredHotelByPages(@RequestBody Filters filters, @RequestParam(value = "page", defaultValue = "1") int page){

        return hotelService.getFilteredHotelByPages(filters, page);
    }


    @GetMapping("/hotel/{id}")
    public Mono<Hotel> getHotelById(@PathVariable Long id){

        return hotelService.getHotelById(id);
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/hotelByOwner/{owner}")
    public Flux<Hotel> getHotelByOwner(@PathVariable String owner){

        return hotelService.getHotelByOwner(owner);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/saveHotel")
    public Mono<Hotel> saveHotel(@RequestBody Mono<Hotel> postHotel){

        return hotelService.saveHotel(postHotel);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/patchHotel")
    public Mono<Hotel> updateHotel(@RequestBody Mono<Hotel> patchHotel){

        return hotelService.patchHotel(patchHotel);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/favourites/{username}")
    public Flux<Hotel> getFavouritesHotelsByUsername(@PathVariable String username){

        return hotelService.getFavouritesHotels(username);
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/deleteHotelById/{hotelId}")
    public Mono<Void> deleteHotelById(@PathVariable Long hotelId){

        return hotelService.deleteHotelById(hotelId);
    }
}