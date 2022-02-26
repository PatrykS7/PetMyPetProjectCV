package PetMyPet.email.repo;
import PetMyPet.email.entities.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepo extends ReactiveCrudRepository<User,String> {

    @Query("UPDATE core.users SET enabled=true WHERE username=:username")
    Mono<User> setEnabledTrue(String username);

    @Query("UPDATE core.users SET password=:password WHERE username=:username")
    Mono<User> updatePassword(String password,String username);
}
