package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.ClosedDay;
import web.petHotel.entities.Hotel;
import web.petHotel.service.ClosedDayService;

@RestController
@RequestMapping("/api")
public class ClosedDayController {

    @Autowired
    ClosedDayService closedDayService;

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/closedDaysByOwner/{username}")
    public Flux<ClosedDay> getClosedDaysByOwner(@PathVariable String username){

        return closedDayService.getClosedDaysByOwner(username);
    }

    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/closedDaysByHotelId/{id}")
    public Flux<ClosedDay> getClosedDayByHotelId(@PathVariable Long id){

        return closedDayService.getClosedDayByHotelId(id);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/saveClosedDay")
    public Mono<ClosedDay> postClosedDay(@RequestBody Mono<ClosedDay> closedDayMono){

        return closedDayService.postClosedDay(closedDayMono);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/patchClosedDay")
    public Mono<ClosedDay> patchClosedDay(@RequestBody Mono<ClosedDay> closedDayMono){

        return closedDayService.patchClosedDay(closedDayMono);
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/deleteClosedDay/{id}")
    public Mono<Void> postClosedDay(@PathVariable Long id){

        return closedDayService.deleteClosedDay(id);
    }
}