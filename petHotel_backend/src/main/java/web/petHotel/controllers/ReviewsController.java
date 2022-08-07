package web.petHotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import web.petHotel.entities.Reviews;
import web.petHotel.service.ReviewsService;

import javax.ws.rs.Path;

@RestController
@RequestMapping("/api")
public class ReviewsController {

    @Autowired
    private ReviewsService reviewsService;

    @GetMapping("/reviews")
    @PreAuthorize("hasAnyRole('OWNER','USER')")
    public Flux<Reviews> getAllReviews(){

        return reviewsService.getAllReviews();
    }

    @GetMapping("/reviewsByUsername/{username}")
    @PreAuthorize("hasRole('USER')")
    public Flux<Reviews> getReviewsByUser(@PathVariable String username){

        return reviewsService.getReviewsByUsername(username);
    }

    @GetMapping("/reviewByUsernameAndHotel/{username}/{hotelId}")
    @PreAuthorize("hasRole('USER')")
    public Mono<Integer> reviewByUsernameAndHotel(@PathVariable String username, @PathVariable Long hotelId){

        return reviewsService.reviewByUsernameAndHotel(username, hotelId);
    }


    @PostMapping("/postReview")
    @PreAuthorize("hasRole('USER')")
    public Mono<Reviews> postReview(@RequestBody Mono<Reviews> review){

        return reviewsService.saveReview(review);
    }

    @PatchMapping("/patchReview")
    @PreAuthorize("hasRole('USER')")
    public Mono<Reviews> patchReview(@RequestBody Mono<Reviews> review){

        return reviewsService.patchReview(review);
    }

    @DeleteMapping("/deleteReview")
    @PreAuthorize("hasRole('USER')")
    public Mono<Reviews> deleteReview(@RequestBody Mono<Reviews> review){

        return reviewsService.deleteReview(review);
    }
}
