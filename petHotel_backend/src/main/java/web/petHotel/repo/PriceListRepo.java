package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.AnimalAndPrice;
import web.petHotel.entities.PriceList;

public interface PriceListRepo extends ReactiveCrudRepository<PriceList,Long> {

    Flux<PriceList> findAllByHotelId(Long hotelId);

    @Query("SELECT animal_type,price FROM core.price_list WHERE hotel_id=:id")
    Flux<AnimalAndPrice> getAnimalAndPriceByHotelId(Long id);

    @Query("UPDATE core.price_list (hotel_id, animal_type, price) " +
            "VALUES (:hotelId, :animalType, :price) " +
            "WHERE hotel_id = :hotelId AND animal_type = :animalType")
    Mono<PriceList> saveOrUpdatePriceList(Long hotelId, Long animalType, Float price);

    @Query("SELECT id FROM core.price_list WHERE hotel_id = :hotelId AND animal_type = :animalType")
    Mono<Long> findIdByHotelAndAnimalType(Long hotelId, Long animalType);

    @Query("DELETE FROM core.price_list WHERE hotel_id = :id")
    Mono<Void> deleteHotelPrices(Long id);
}
