package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.ReservationWithData;
import web.petHotel.entities.Reservation;
import web.petHotel.service.ReservationService;

import javax.persistence.ManyToOne;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("/reservation/{id}")
    public Mono<Reservation> getReservationById(@PathVariable Long id){

        return reservationService.getReservationById(id);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/saveReservation")
    public Mono<Reservation> saveReservation(@RequestBody Mono<Reservation> postReservation){

        return reservationService.saveReservation(postReservation);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/patchReservation")
    public Mono<Reservation> updateReservation(@RequestBody Mono<Reservation> patchReservation){

        return reservationService.patchReservation(patchReservation);
    }

    //reservation with details for user
    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/userReservations/{username}")
    public Flux<ReservationWithData> getReservationsByUsernameWithDetails(@PathVariable String username){

        return reservationService.getReservationsByUsernameWithDetails(username);
    }

    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/ownerReservations/{owner}")
    public Flux<ReservationWithData> getReservationsByOwnerWithDetails(@PathVariable String owner){

        return reservationService.getReservationsByOwnerWithDetails(owner);
    }

    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/userFinishedReservations/{username}")
    public Flux<ReservationWithData> getFinishedReservationsByUsernameWithDetails(@PathVariable String username){

        return reservationService.getFinishedReservationsByUsernameWithDetails(username);
    }

    @PreAuthorize("hasAnyRole('OWNER','USER')")
    @GetMapping("/ownerFinishedReservations/{owner}")
    public Flux<ReservationWithData> getFinishedReservationsByOwnerWithDetails(@PathVariable String owner){

        return reservationService.getFinishedReservationsByOwnerWithDetails(owner);
    }
}
