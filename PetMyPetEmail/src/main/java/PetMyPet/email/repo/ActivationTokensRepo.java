package PetMyPet.email.repo;

import PetMyPet.email.entities.ActivationToken;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ActivationTokensRepo extends ReactiveCrudRepository<ActivationToken,Long> {

    Mono<ActivationToken> findByToken(String token);

    @Query("DELETE FROM core.activation_tokens WHERE token=:token")
    Mono<ActivationToken> deleteTokenEntry(String token);

    @Query("INSERT INTO core.activation_tokens (username, token, created_at)\n" +
            "VALUES(:username,:token, :createdAt) \n" +
            "ON CONFLICT (username) \n" +
            "DO UPDATE \n" +
            "\tSET token = :token,\n" +
            "\t\tcreated_at = :createdAt;")
    Mono<ActivationToken> saveOrReplace(String username, String token, LocalDateTime createdAt);
}
