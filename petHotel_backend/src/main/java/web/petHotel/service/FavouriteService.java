package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import web.petHotel.JSON.UsernameAndHotel;
import web.petHotel.repo.FavouritesRepo;

@Service
public class FavouriteService {

    private final FavouritesRepo favouritesRepo;

    @Autowired
    public FavouriteService(FavouritesRepo favouritesRepo) {
        this.favouritesRepo = favouritesRepo;
    }

    public Mono<Void> deleteFavouriteHotel(UsernameAndHotel usernameAndHotel) {

        return favouritesRepo.deleteFavouriteHotel(usernameAndHotel.getUsername(),usernameAndHotel.getHotelId());
    }

    public Mono<Void> addFavouriteHotel(UsernameAndHotel usernameAndHotel) {

        return favouritesRepo.addFavouriteHotel(usernameAndHotel.getUsername(),usernameAndHotel.getHotelId());
    }
}
