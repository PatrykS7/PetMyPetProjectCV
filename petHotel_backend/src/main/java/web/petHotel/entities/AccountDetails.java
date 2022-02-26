package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("core.account_details")
public class AccountDetails implements Persistable<String> {

    @Id
    private String username;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    private String street;

    private String zipcode;

    private String city;

    @Column("phone_number")
    private String phoneNumber;

    @Transient
    @JsonIgnore
    private boolean isNew = true;

    public AccountDetails(String username, String firstName, String lastName, String street, String zipcode, String city, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }

    public AccountDetails(String username, String firstName, String lastName, String street, String zipcode, String city, String phoneNumber, boolean isNew) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipcode = zipcode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.isNew = isNew;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }
}
