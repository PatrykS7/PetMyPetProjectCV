package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.ClosedDay;
import web.petHotel.repo.ClosedDayRepo;
import web.petHotel.repo.HotelRepo;

import java.util.function.Function;

@Service
public class ClosedDayService {

    private final ClosedDayRepo closedDayRepo;
    private final HotelRepo hotelRepo;

    @Autowired
    public ClosedDayService(ClosedDayRepo closedDayRepo, HotelRepo hotelRepo) {
        this.closedDayRepo = closedDayRepo;
        this.hotelRepo = hotelRepo;
    }

    public Flux<ClosedDay> findAll() {

        return closedDayRepo.findAll();
    }

    public Mono<ClosedDay> postClosedDay(Mono<ClosedDay> closedDayMono) {

        return closedDayRepo.saveAll(closedDayMono).next();
    }


    public Mono<ClosedDay> patchClosedDay(Mono<ClosedDay> closedDayMono) {

        return closedDayMono.map( c -> {
            c.setNew(false);
            return c;
        })
                .flatMap( j -> closedDayRepo.save(j));
    }

    public Mono<Void> deleteClosedDay(Long id) {

        return closedDayRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete Closed Day")))
                .flatMap(closedDayRepo::delete);
    }

    public Flux<ClosedDay> getClosedDaysByOwner(String username) {

        return closedDayRepo.getClosedDayByOwner(username)
                .flatMap( closedDay -> {
                     return Mono.just(closedDay)
                            .zipWith(hotelRepo.findHotelNameById(closedDay.getHotelId()))
                            .map( zip -> {
                                zip.getT1().setHotelName(zip.getT2());
                                return zip.getT1();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Closed Days")));
    }

    public Flux<ClosedDay> getClosedDayByHotelId(Long id) {

        return closedDayRepo.findClosedDayByHotelId(id)
                .flatMap( closedDay -> {
                    return Mono.just(closedDay)
                            .zipWith(hotelRepo.findHotelNameById(closedDay.getHotelId()))
                            .map( zip -> {
                                zip.getT1().setHotelName(zip.getT2());
                                return zip.getT1();
                            });
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find Closed Days")));
    }
}
