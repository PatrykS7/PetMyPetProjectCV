package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import web.petHotel.JSON.HotelScores;

import java.util.HashMap;
import java.util.Map;

@Data
@Table("core.hotels")
public class Hotel implements Persistable<Long> {

    @Id
    private Long id;

    private String name;
    private String description;
    private String street;
    private String zipcode;
    private String city;
    @Column("phone_number")
    private String phoneNumber;
    @Column("contact_email")
    private String contactEmail;
    private String owner;

    private Double lon;
    private Double lat;

    @Transient
    private Map<Long, Float> prices = new HashMap<>();

    @Transient
    private HotelScores hotelScores;

    @JsonIgnore
    @Transient
    private boolean isNew = true;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }

}
