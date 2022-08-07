package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;
import web.petHotel.JSON.HotelIdWithPriceMap;
import web.petHotel.entities.PriceList;
import web.petHotel.repo.PriceListRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PriceListService {

    private final PriceListRepo priceListRepo;

    @Autowired
    public PriceListService(PriceListRepo priceListRepo) {
        this.priceListRepo = priceListRepo;
    }


    public Flux<PriceList> getPriceListByHotelId(Long id) {

        return priceListRepo.findAllByHotelId(id);
    }

    public Flux<PriceList> patchPriceList(HotelIdWithPriceMap hotelWithPrice) {

        Mono<Map<Long, Float>> priceMapFlux = Mono.just(hotelWithPrice.getPrices());

        return priceListRepo.deleteHotelPrices(hotelWithPrice.getHotelId())
                .thenMany(
                             priceMapFlux.flatMapMany(map -> Flux.fromIterable(map.entrySet()))
                                    .map(e -> Tuples.of(e.getKey(), e.getValue()))
                                    .flatMap(tuple -> Mono.just(tuple)
                                            .zipWith(priceListRepo.findIdByHotelAndAnimalType(hotelWithPrice.getHotelId(), tuple.getT1()))
                                            .map(zip -> {
                                                return new PriceList(zip.getT2(), hotelWithPrice.getHotelId(), tuple.getT1(), tuple.getT2(), false);
                                            })
                                            .switchIfEmpty(
                                                    Mono.just(new PriceList(null, hotelWithPrice.getHotelId(), tuple.getT1(), tuple.getT2(), true)))
                                            .flatMap(priceListRepo::save))
                        );
//        return priceMapFlux.flatMapMany(map -> Flux.fromIterable(map.entrySet()))
//                .map(e -> Tuples.of(e.getKey(), e.getValue()))
//                .flatMap( tuple -> Mono.just(tuple)
//                        .zipWith(priceListRepo.findIdByHotelAndAnimalType(hotelWithPrice.getHotelId(), tuple.getT1()))
//                        .map( zip -> {
//                            return  new PriceList(zip.getT2(), hotelWithPrice.getHotelId(), tuple.getT1(), tuple.getT2(), false);
//                        })
//                        .switchIfEmpty(
//                                Mono.just(new PriceList(null, hotelWithPrice.getHotelId(), tuple.getT1(), tuple.getT2(), true))
//                        )
//                        .flatMap(priceListRepo::save));
    }
}
