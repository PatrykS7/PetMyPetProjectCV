package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;
import web.petHotel.entities.CompanyInfo;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithDetailsWithCompany extends UserWithDetails{

    private String name;
    private String nip;
    private String regon;

    public UserWithDetailsWithCompany(String username, String password, boolean enabled, String authority, String firstName, String lastName, String street, String zipcode, String city, String phoneNumber, String username1, String password1, boolean enabled1, String authority1, String firstName1, String lastName1, String street1, String zipcode1, String city1, Long phoneNumber1, String name, String nip, String regon) {
        super(username, password, enabled, authority, firstName, lastName, street, zipcode, city, phoneNumber);
        this.name = name;
        this.nip = nip;
        this.regon = regon;
    }

    public Mono<CompanyInfo> convertToCompanyInfo(){
        return Mono.just(
                new CompanyInfo(getUsername(),this.name,this.nip,this.regon)
        );
    }
}
