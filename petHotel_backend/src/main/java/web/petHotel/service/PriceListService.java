package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import web.petHotel.entities.PriceList;
import web.petHotel.repo.PriceListRepo;

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
}
