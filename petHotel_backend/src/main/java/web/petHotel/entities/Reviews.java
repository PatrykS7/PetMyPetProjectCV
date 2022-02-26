package web.petHotel.entities;

import lombok.Data;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.function.Tuple2;

@Data
@Table("core.reviews")
public class Reviews{

    private String username;
    @Column("hotel_id")
    private Long hotelId;
    private Integer score;
}
