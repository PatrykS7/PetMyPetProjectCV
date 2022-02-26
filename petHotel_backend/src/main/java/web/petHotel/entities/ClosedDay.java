package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Table("core.closed_days")
public class ClosedDay implements Persistable<Long>{

    @Id
    private Long id;

    @Column("hotel_id")
    private Long hotelId;

    @Column("animal_type")
    private Long animalType;

    @Column("starting_date")
    private LocalDate startingDate;

    @Column("ending_date")
    private LocalDate endingDate;

    @Transient
    private String hotelName;

    @JsonIgnore
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
