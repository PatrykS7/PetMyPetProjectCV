package web.petHotel.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import web.petHotel.entities.User;

public interface UserRepo extends ReactiveCrudRepository<User,String> {

}
