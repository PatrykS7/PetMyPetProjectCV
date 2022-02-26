package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("core.reservations")
public class Reservation implements Persistable<Long> {

    @Id
    @Column("id")
    private Long id;

    @Column("hotel_id")
    private Long hotelId;

    @Column("animal_id")
    private Long animalId;

    @Column("check_in")
    private LocalDate checkIn;

    @Column("check_out")
    private LocalDate checkOut;

    @Column("status")
    private String status;

    @Transient
    @JsonIgnore
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}