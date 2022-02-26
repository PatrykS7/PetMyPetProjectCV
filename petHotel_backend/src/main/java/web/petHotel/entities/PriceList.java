package web.petHotel.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("core.price_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceList {

    @Id
    private Long id;

    @Column("hotel_id")
    private Long hotelId;

    @Column("animal_type")
    private Long animalType;

    private Float price;
}
