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
@Table("core.animals")
public class Animal implements Persistable<Long> {

    @Id
    private Long id;

    private String name;
    private Float weight;
    @Column("animal_type")
    private Long animalType;
    @Column("birth_date")
    private LocalDate birthDate;
    private String notes;
    private boolean vaccinations;
    private boolean visibility = true;
    private String owner;
    @Column("accommodated_in")
    private Long accommodatedIn;

    @Transient
    @JsonIgnore
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
