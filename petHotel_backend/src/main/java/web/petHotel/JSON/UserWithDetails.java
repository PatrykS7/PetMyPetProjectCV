package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
import web.petHotel.entities.AccountDetails;
import web.petHotel.entities.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithDetails {

    private String username;
    private String password;
    private boolean enabled = true;
    private String authority;
    private String firstName;
    private String lastName;
    private String street;
    private String zipcode;
    private String city;
    private String phoneNumber;

    public Mono<User> convertToUser(){

        return Mono.just(
                new User(this.username, this.password, this.enabled, this.authority)
        );
    }

    public Mono<AccountDetails> convertToAccountDetails(){

        return Mono.just(
                new AccountDetails(this.username, this.firstName, this.lastName, this.street, this.zipcode, this.city, this.phoneNumber)
        );
    }
}
