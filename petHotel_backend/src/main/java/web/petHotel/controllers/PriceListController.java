package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.HotelIdWithPriceMap;
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

    @PatchMapping("/patchPriceList")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public Flux<PriceList> patchPriceList(@RequestBody HotelIdWithPriceMap hotelWithPrice){

        return priceListService.patchPriceList(hotelWithPrice);
    }

}
