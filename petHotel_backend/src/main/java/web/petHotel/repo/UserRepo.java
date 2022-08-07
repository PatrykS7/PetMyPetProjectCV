package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import web.petHotel.entities.User;

public interface UserRepo extends ReactiveCrudRepository<User,String> {

    @Query("DELETE FROM core.users WHERE username = :username")
    Mono<Void> deleteUserByUsername(String username);

}
