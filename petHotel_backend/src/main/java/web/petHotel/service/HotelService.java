package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Hotel;
import web.petHotel.repo.HotelRepo;
import web.petHotel.repo.PriceListRepo;
import web.petHotel.repo.ReviewsRepo;

@Service
public class HotelService {

    private final HotelRepo hotelRepo;
    private final PriceListRepo priceListRepo;
    private final ReviewsRepo reviewsRepo;

    @Autowired
    public HotelService(HotelRepo hotelRepo, PriceListRepo priceListRepo, ReviewsRepo reviewsRepo) {
        this.hotelRepo = hotelRepo;
        this.priceListRepo = priceListRepo;
        this.reviewsRepo = reviewsRepo;
    }

    public Flux<Hotel> getAllHotels(){

        return hotelRepo.findAll();
    }

    public Mono<Hotel> saveHotel(Mono<Hotel> postHotel) {

        return hotelRepo.saveAll(postHotel).next()
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to create hotel")));
    }

    public Mono<Hotel> patchHotel(Mono<Hotel> patchHotel) {

        return patchHotel.map( h -> {
            h.setNew(false);
            return h;
        })
                .flatMap( j -> hotelRepo.save(j));
    }

    public Mono<Hotel> getHotelById(Long id) {

        return hotelRepo.findById(id)
                .flatMap( hotel -> {
                    return Mono.just( hotel )
                            .zipWith(priceListRepo.findAllByHotelId(hotel.getId()).collectMap(e -> e.getAnimalType(), f -> f.getPrice()))
                            .map( zip -> {
                                zip.getT1().setPrices(zip.getT2());
                                return zip.getT1();
                            })
                            .zipWith(reviewsRepo.getHotelScoresById(hotel.getId()))
                            .map( zip2 -> {
                                zip2.getT1().setHotelScores(zip2.getT2());
                                return zip2.getT1();
                            });
                })
                .switchIfEmpty( Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Hotel with that id doesn't exist")));
    }

    public Flux<Hotel> getHotelByOwner(String owner) {

        return hotelRepo.findHotelByOwner(owner)
                .flatMap( hotel -> {
                    return Mono.just( hotel )
                            .zipWith(priceListRepo.findAllByHotelId(hotel.getId()).collectMap(e -> e.getAnimalType(), f -> f.getPrice()))
                            .map( zip -> {
                                zip.getT1().setPrices(zip.getT2());
                                return zip.getT1();
                            })
                            .zipWith(reviewsRepo.getHotelScoresById(hotel.getId()))
                            .map( zip2 -> {
                                zip2.getT1().setHotelScores(zip2.getT2());
                                return zip2.getT1();
                            });
                })
                .switchIfEmpty( Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to find hotel by that owner")));
    }

    public Flux<Hotel> getFavouritesHotels(String username){

        return hotelRepo.selectFauvoritesHotelsByUsername(username)
                .flatMap( hotel -> {
                    return Mono.just( hotel )
                            .zipWith(priceListRepo.findAllByHotelId(hotel.getId()).collectMap(e -> e.getAnimalType(), f -> f.getPrice()))
                            .map( zip -> {
                                zip.getT1().setPrices(zip.getT2());
                                return zip.getT1();
                            })
                            .zipWith(reviewsRepo.getHotelScoresById(hotel.getId()))
                            .map( zip2 -> {
                                zip2.getT1().setHotelScores(zip2.getT2());
                                return zip2.getT1();
                            });
                });
    }

    public Flux<Hotel> getHotelByPages(int page) {

        return hotelRepo.findHotelByPages(12,(page-1)*12)
                .flatMap( hotel -> {
                    return Mono.just( hotel )
                            .zipWith(priceListRepo.findAllByHotelId(hotel.getId()).collectMap(e -> e.getAnimalType(), f -> f.getPrice()))
                            .map( zip -> {
                                zip.getT1().setPrices(zip.getT2());
                                return zip.getT1();
                            })
                            .zipWith(reviewsRepo.getHotelScoresById(hotel.getId()))
                            .map( zip2 -> {
                                zip2.getT1().setHotelScores(zip2.getT2());
                                return zip2.getT1();
                            });
                });
    }
}
