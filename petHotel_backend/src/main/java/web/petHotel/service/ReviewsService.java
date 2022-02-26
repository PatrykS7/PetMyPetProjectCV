package web.petHotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import web.petHotel.entities.Reviews;
import web.petHotel.repo.ReviewsRepo;

@Service
public class ReviewsService {

    private final ReviewsRepo reviewsRepo;

    @Autowired
    public ReviewsService(ReviewsRepo reviewsRepo) {
        this.reviewsRepo = reviewsRepo;
    }

    public Flux<Reviews> getAllReviews() {

        return reviewsRepo.getAll();
    }

    public Mono<Reviews> saveReview(Mono<Reviews> review) {

        return review.flatMap( rev ->
                        reviewsRepo.insertReview(rev.getUsername(),rev.getHotelId(), rev.getScore()))
                    .onErrorResume( Exception.class, e ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to save review")));
    }

    public Mono<Reviews> patchReview(Mono<Reviews> review) {

        return review.flatMap( rev ->
                        reviewsRepo.updateReview(rev.getScore(),rev.getUsername(),rev.getHotelId()))
                .onErrorResume( Exception.class, e ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to update review")));
    }

    public Mono<Reviews> deleteReview(Mono<Reviews> review) {

        return review.flatMap( rev ->
                        reviewsRepo.deleteReview(rev.getUsername(),rev.getHotelId()));
    }
}