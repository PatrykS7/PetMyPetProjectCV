package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import web.petHotel.JSON.AnimalAndPrice;
import web.petHotel.entities.PriceList;

public interface PriceListRepo extends ReactiveCrudRepository<PriceList,Long> {

    Flux<PriceList> findAllByHotelId(Long hotelId);

    @Query("SELECT animal_type,price FROM core.price_list WHERE hotel_id=:id")
    Flux<AnimalAndPrice> getAnimalAndPriceByHotelId(Long id);
}
