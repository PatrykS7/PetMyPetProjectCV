package web.petHotel.service;

import com.google.maps.errors.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.Filters;
import web.petHotel.entities.Hotel;
import web.petHotel.entities.PriceList;
import web.petHotel.repo.HotelRepo;
import web.petHotel.repo.PriceListRepo;
import web.petHotel.repo.ReviewsRepo;
import web.petHotel.scripts.Scripts;

import java.io.IOException;
import java.util.ArrayList;

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


    public Mono<Hotel> saveHotel(Mono<Hotel> postHotel) {

        return postHotel.map( hotel -> {

            try {
                GeocodingService geo = new GeocodingService(StringUtils.stripAccents(hotel.getCity()) + " " + StringUtils.stripAccents(hotel.getStreet()));
                hotel.setLat(geo.getLat());
                hotel.setLon(geo.getLng());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hotel;
        })
                .flatMap(hotelRepo::save)
                .onErrorResume(Exception.class, t ->
                    Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to create hotel")));
    }

    public Mono<Hotel> patchHotel(Mono<Hotel> patchHotel) {

        return patchHotel.flatMap( h -> {
            h.setNew(false);
            return Mono.just(h)
                    .zipWith(hotelRepo.findById(h.getId()))
                    .map(zip -> {
                        try {
                            GeocodingService geo = new GeocodingService(StringUtils.stripAccents(zip.getT1().getCity()) + " " + StringUtils.stripAccents(zip.getT1().getStreet()));
                            zip.getT1().setLon(geo.getLng());
                            zip.getT1().setLat(geo.getLat());

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        return zip.getT1();
                    })
                    .flatMap(hotelRepo::save);
        });

    }

    public Mono<Hotel> getHotelById(Long id) {

        return hotelRepo.findById(id)
                .flatMap(this::mapHotelData)
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find hotel")));
    }

    public Flux<Hotel> getHotelByOwner(String owner) {

        return hotelRepo.findAllByOwner(owner)
                .flatMap(this::mapHotelData)
                .switchIfEmpty( Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to find hotel by that owner")));
    }

    public Flux<Hotel> getFavouritesHotels(String username){

        return hotelRepo.selectFauvoritesHotelsByUsername(username)
                .flatMap(this::mapHotelData);
    }

    public Flux<Hotel> getHotelByPages(int page) {

        return hotelRepo.findHotelByPages(12,(page-1)*12)
                .flatMap(this::mapHotelData);
    }

    public Flux<Hotel> getFilteredHotelByPages(Filters filters, int page){

        return Flux.just(filters)
                .flatMap( (filters1) -> {
                    GeocodingService geo = null;
                    try {
                        geo = new GeocodingService(StringUtils.stripAccents(filters.getLocation()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    return hotelRepo.findHotelsByFiltersByPages(geo.getLng(),geo.getLat(),filters.getRange() * 1000, filters.getStartingDate(), filters.getEndingDate(), filters.getAnimalType(), 12,(page-1)*12);

                })
                .flatMap(this::mapHotelData)
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find hotels")));

    }

    public Mono<Void> deleteHotelById(Long hotelId) {

        return hotelRepo.deleteHotelById(hotelId);
    }

    //support functions

    private Mono<Hotel> mapHotelData(Hotel hotel){

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
    }
}
