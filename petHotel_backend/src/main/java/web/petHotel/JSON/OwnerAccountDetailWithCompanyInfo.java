package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
import web.petHotel.entities.AccountDetails;
import web.petHotel.entities.CompanyInfo;

@Data
@AllArgsConstructor
public class OwnerAccountDetailWithCompanyInfo {

    private String username;
    private String firstName;
    private String lastName;
    private String street;
    private String zipcode;
    private String city;
    private String phoneNumber;
    private String name;
    private String nip;
    private String regon;


    public Mono<AccountDetails> convertToAccountDetails(){

        return Mono.just(
                new AccountDetails(this.username, this.firstName, this.lastName, this.street, this.zipcode, this.city, this.phoneNumber, false)
        );
    }

    public Mono<CompanyInfo> convertToCompanyInfo(){

        return Mono.just(
                new CompanyInfo(this.username, this.name, this.nip, this.regon, false)
        );
    }
}
