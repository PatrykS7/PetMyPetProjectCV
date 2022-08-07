package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.NotificationEmailData;
import web.petHotel.JSON.ReservationWithData;
import web.petHotel.JSON.ReservationWithDataWithPhoneNum;
import web.petHotel.entities.PriceList;
import web.petHotel.entities.Reservation;
import web.petHotel.repo.*;

@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final AnimalRepo animalRepo;
    private final HotelRepo hotelRepo;
    private final PriceListRepo priceListRepo;
    private final AccountDetailsRepo accountDetailsRepo;

    @Autowired
    public ReservationService(ReservationRepo reservationRepo, AnimalRepo animalRepo, HotelRepo hotelRepo, PriceListRepo priceListRepo, AccountDetailsRepo accountDetailsRepo) {
        this.reservationRepo = reservationRepo;
        this.animalRepo = animalRepo;
        this.hotelRepo = hotelRepo;
        this.priceListRepo = priceListRepo;
        this.accountDetailsRepo = accountDetailsRepo;
    }

    @Autowired
    private WebClient.Builder webClient;

    public Flux<Reservation> getAllReservations() {

        return reservationRepo.findAll();
    }

    public Mono<Reservation> getReservationById(Long id) {

        return reservationRepo.findById(id)
                .switchIfEmpty( Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Reservation with that id doesn't exist")));
    }

    public Mono<Reservation> saveReservation(Mono<Reservation> postReservation) {

        return reservationRepo.saveAll(postReservation).next()
                .flatMap( reservation -> mapReservationDataForOwnerMail(reservation)
                        .flatMap( notificationData -> sendEmail(notificationData, "add"))
                        .thenReturn(reservation)
                )
                .onErrorResume(DataIntegrityViolationException.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to create reservation")));
    }

    public Mono<Reservation> patchReservation(Mono<Reservation> patchReservation) {

        return patchReservation.map( r -> {
            r.setNew(false);
            return r;
        })
                .flatMap(reservationRepo::save)
                .flatMap( reservation -> mapReservationDataForUserMail(reservation)
                        .flatMap( notificationData -> sendEmail(notificationData, "patch"))
                        .thenReturn(reservation)
                );
    }

    public Flux<ReservationWithData> getReservationsByUsernameWithDetails(String username) { //opus magnum

        return reservationRepo.getReservationByUsername(username)
                .flatMap(this::mapReservationDataForUserGet)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Flux<ReservationWithDataWithPhoneNum> getReservationsByOwnerWithDetails(String owner) {

        return reservationRepo.getOwnerReservations(owner)
                .flatMap(this::mapReservationDataForOwnerGet)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Flux<ReservationWithData> getFinishedReservationsByUsernameWithDetails(String username) {

        return reservationRepo.getFinishedReservationByUsername(username)
                .flatMap(this::mapReservationDataForUserGet)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Flux<ReservationWithDataWithPhoneNum> getFinishedReservationsByOwnerWithDetails(String owner) {

        return reservationRepo.getFinishedOwnerReservations(owner)
                .flatMap(this::mapReservationDataForOwnerGet)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find reservation")));
    }

    public Mono<Void> deleteById(Long id) {

        return reservationRepo.findById(id)
                .flatMap(this::mapReservationDataForOwnerMail)
                .flatMap( notificationData -> sendEmail(notificationData, "delete"))
                .then(reservationRepo.deleteById(id));
    }

    //support functions

    private Flux<ReservationWithData> mapReservationDataForUserGet(Reservation reservation){ //for get request

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
                .zipWith(priceListRepo.findAllByHotelId(reservation.getHotelId()).collectMap(PriceList::getAnimalType, PriceList::getPrice))
                .map( zipWithPrices -> {
                    zipWithPrices.getT1().getHotel().setPrices(zipWithPrices.getT2());
                    return zipWithPrices.getT1();
                })
                .zipWith(animalRepo.findAnimalById(reservation.getAnimalId()))
                .map( zip2 -> {
                    zip2.getT1().setAnimal(zip2.getT2());
                    return zip2.getT1();
                });
    }

    private Flux<ReservationWithDataWithPhoneNum> mapReservationDataForOwnerGet(Reservation reservation){ //for get request

        return Flux.just(
                        new ReservationWithDataWithPhoneNum(
                                reservation.getId(),
                                null,
                                null,
                                reservation.getCheckIn(),
                                reservation.getCheckOut(),
                                reservation.getStatus(),
                                null
                        ))
                .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                .map( zip1 -> {
                    zip1.getT1().setHotel(zip1.getT2());
                    return zip1.getT1();
                })
                .zipWith(priceListRepo.findAllByHotelId(reservation.getHotelId()).collectMap(PriceList::getAnimalType, PriceList::getPrice))
                .map( zipWithPrices -> {
                    zipWithPrices.getT1().getHotel().setPrices(zipWithPrices.getT2());
                    return zipWithPrices.getT1();
                })
                .zipWith(animalRepo.findAnimalById(reservation.getAnimalId()))
                .map( zip2 -> {
                    zip2.getT1().setAnimal(zip2.getT2());
                    return zip2.getT1();
                })
                .flatMap( res -> Mono.just(res) //add phone number
                        .zipWith(accountDetailsRepo.getPhoneNumberByUsername(res.getAnimal().getOwner()))
                        .map( resWithPhone -> {
                            resWithPhone.getT1().setOwnerPhoneNumber(resWithPhone.getT2());
                            return resWithPhone.getT1();
                        }));
    }

    private Mono<NotificationEmailData> mapReservationDataForOwnerMail(Reservation reservation){ // for email

         return Mono.just(new NotificationEmailData( // for email
                        null,
                        null,
                        null,
                        null,
                        reservation.getCheckIn(),
                        reservation.getCheckOut(),
                         null
                ))
                .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                .map( zip1 -> {
                    zip1.getT1().setHotelName(zip1.getT2().getName());
                    zip1.getT1().setRecipient(zip1.getT2().getOwner());
                    return zip1.getT1();
                })
                .zipWith(animalRepo.findAnimalNameWithTypeById(reservation.getAnimalId()))
                .map( zip2 -> {
                    zip2.getT1().setAnimalName(zip2.getT2().getName());
                    zip2.getT1().setAnimalTypeStr(zip2.getT2().getTypeName());
                    return zip2.getT1();
                });
    }

    private Mono<NotificationEmailData> mapReservationDataForUserMail(Reservation reservation){ // for email

        return Mono.just(new NotificationEmailData( // for email
                        null,
                        null,
                        null,
                        null,
                        reservation.getCheckIn(),
                        reservation.getCheckOut(),
                        reservation.getStatus()
                ))
                .zipWith(hotelRepo.findHotelById(reservation.getHotelId()))
                .map( zip1 -> {
                    zip1.getT1().setHotelName(zip1.getT2().getName());
                    return zip1.getT1();
                })
                .zipWith(animalRepo.findAnimalNameWithTypeById(reservation.getAnimalId()))
                .map( zip2 -> {
                    zip2.getT1().setRecipient(zip2.getT2().getOwner());
                    zip2.getT1().setAnimalName(zip2.getT2().getName());
                    zip2.getT1().setAnimalTypeStr(zip2.getT2().getTypeName());
                    return zip2.getT1();
                });
    }

    private Mono<Object> sendEmail(NotificationEmailData notificationData,String type){

        return webClient.build()
                .post()
                .uri("http://EMAIL-SERVICE/email/notification/" + type)
                .body(Mono.just(notificationData), NotificationEmailData.class)
                .retrieve()
                .bodyToMono(Object.class);
    }

}