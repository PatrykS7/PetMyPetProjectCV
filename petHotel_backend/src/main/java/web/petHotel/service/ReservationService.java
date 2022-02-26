package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.ReservationWithData;
import web.petHotel.entities.Reservation;
import web.petHotel.repo.AnimalRepo;
import web.petHotel.repo.HotelRepo;
import web.petHotel.repo.ReservationRepo;

@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final AnimalRepo animalRepo;
    private final HotelRepo hotelRepo;

    @Autowired
    public ReservationService(ReservationRepo reservationRepo, AnimalRepo animalRepo, HotelRepo hotelRepo) {
        this.reservationRepo = reservationRepo;
        this.animalRepo = animalRepo;
        this.hotelRepo = hotelRepo;
    }

    public Flux<Reservation> getAllReservations() {

        return reservationRepo.findAll();
    }

    public Mono<Reservation> getReservationById(Long id) {

        return reservationRepo.findById(id)
                .switchIfEmpty( Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Reservation with that id doesn't exist")));
    }

    public Mono<Reservation> saveReservation(Mono<Reservation> postReservation) {

        return reservationRepo.saveAll(postReservation).next()
                .onErrorResume(DataIntegrityViolationException.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to create reservation")));
    }

    public Mono<Reservation> patchReservation(Mono<Reservation> patchReservation) {

        return patchReservation.map( r -> {
            r.setNew(false);
            return r;
        })
                .flatMap( s -> reservationRepo.save(s));
    }

    public Flux<ReservationWithData> getReservationsByUsernameWithDetails(String username) { //opus magnum

        return reservationRepo.getReservationByUsername(username)
                .flatMap( reservation -> {
                    return Flux.just(
                            new ReservationWithData(
                            reservation.getId(),
                            null,
                            null,
                            reservation.getCheckIn(),
                            reservation.getCheckOut(),
                            reservation.getStatus()
                    ))
                            .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                            .map( zip1 -> {
                                zip1.getT1().setHotel(zip1.getT2());
                                return zip1.getT1();
                            })
                            .zipWith(animalRepo.findAnimalById(reservation.getAnimalId()))
                            .map( zip2 -> {
                                zip2.getT1().setAnimal(zip2.getT2());
                                return zip2.getT1();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Flux<ReservationWithData> getReservationsByOwnerWithDetails(String owner) {

        return reservationRepo.getOwnerReservations(owner)
                .flatMap( reservation -> {
                    return Flux.just(
                                    new ReservationWithData(
                                            reservation.getId(),
                                            null,
                                            null,
                                            reservation.getCheckIn(),
                                            reservation.getCheckOut(),
                                            reservation.getStatus()
                                    ))
                            .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                            .map( zip1 -> {
                                zip1.getT1().setHotel(zip1.getT2());
                                return zip1.getT1();
                            })
                            .zipWith(animalRepo.findAnimalById(reservation.getAnimalId()))
                            .map( zip2 -> {
                                zip2.getT1().setAnimal(zip2.getT2());
                                return zip2.getT1();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Flux<ReservationWithData> getFinishedReservationsByUsernameWithDetails(String username) {

        return reservationRepo.getFinishedReservationByUsername(username)
                .flatMap( reservation -> {
                    return Flux.just(
                                    new ReservationWithData(
                                            reservation.getId(),
                                            null,
                                            null,
                                            reservation.getCheckIn(),
                                            reservation.getCheckOut(),
                                            reservation.getStatus()
                                    ))
                            .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                            .map( zip1 -> {
                                zip1.getT1().setHotel(zip1.getT2());
                                return zip1.getT1();
                            })
                            .zipWith(animalRepo.findAnimalById(reservation.getAnimalId()))
                            .map( zip2 -> {
                                zip2.getT1().setAnimal(zip2.getT2());
                                return zip2.getT1();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Flux<ReservationWithData> getFinishedReservationsByOwnerWithDetails(String owner) {

        return reservationRepo.getFinishedOwnerReservations(owner)
                .flatMap( reservation -> {
                    return Flux.just(
                                    new ReservationWithData(
                                            reservation.getId(),
                                            null,
                                            null,
                                            reservation.getCheckIn(),
                                            reservation.getCheckOut(),
                                            reservation.getStatus()
                                    ))
                            .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                            .map( zip1 -> {
                                zip1.getT1().setHotel(zip1.getT2());
                                return zip1.getT1();
                            })
                            .zipWith(animalRepo.findAnimalById(reservation.getAnimalId()))
                            .map( zip2 -> {
                                zip2.getT1().setAnimal(zip2.getT2());
                                return zip2.getT1();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }
}