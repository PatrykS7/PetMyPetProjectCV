package web.petHotel.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import web.petHotel.entities.AccountDetails;

public interface AccountDetailsRepo extends ReactiveCrudRepository<AccountDetails,String> {

    Mono<AccountDetails> findByUsername(String username);

}
