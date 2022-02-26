package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import web.petHotel.entities.PriceList;
import web.petHotel.service.PriceListService;

@RestController
@RequestMapping("/api")
public class PriceListController {

    @Autowired
    private PriceListService priceListService;

    @GetMapping("/getPriceListByHotelId/{id}")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public Flux<PriceList> getPriceListByHotelId(@PathVariable Long id){

       return priceListService.getPriceListByHotelId(id);
    }
}
