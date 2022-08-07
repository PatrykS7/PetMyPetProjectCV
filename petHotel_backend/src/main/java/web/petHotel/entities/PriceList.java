package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("core.price_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceList implements Persistable<Long> {

    @Id
    private Long id;
    @Column("hotel_id")
    private Long hotelId;
    @Column("animal_type")
    private Long animalType;
    private Float price;

    @JsonIgnore
    @Transient
    private boolean isNew = true;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }

    public PriceList(Long hotelId, Long animalType, Float price, boolean isNew) {
        this.hotelId = hotelId;
        this.animalType = animalType;
        this.price = price;
        this.isNew = isNew;
    }

}
