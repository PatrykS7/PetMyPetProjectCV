package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import web.petHotel.JSON.HotelScores;
import web.petHotel.entities.Reviews;

public interface ReviewsRepo extends ReactiveCrudRepository<Reviews, Tuple2<String, Long>> {

    @Query("SELECT * FROM core.reviews")
    Flux<Reviews> getAll();

    @Query("SELECT * FROM core.reviews WHERE username=:username")
    Flux<Reviews> getAllByUsername(String username);

    @Query("SELECT score FROM core.reviews WHERE username=:username AND hotel_id=:hotelId")
    Mono<Integer> getReviewByUsernameAndHotelId(String username, Long hotelId);

    @Query("INSERT INTO core.reviews VALUES (:username, :hotelId, :score)")
    Mono<Reviews> insertReview(String username, Long hotelId, Integer score);

    @Query("UPDATE core.reviews SET score=:newScore WHERE username=:username AND hotel_id=:hotelId")
    Mono<Reviews> updateReview(Integer newScore, String username, Long hotelId);

    @Query("DELETE FROM core.reviews WHERE username=:username AND hotel_id=:hotelId")
    Mono<Reviews> deleteReview(String username, Long hotelId);

    @Query("SELECT score,reviews_amount FROM core.hotels_scores WHERE id=:id")
    Mono<HotelScores> getHotelScoresById(Long id);
}
