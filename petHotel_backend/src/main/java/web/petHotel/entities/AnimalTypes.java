package web.petHotel.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("core.animal_types")
public class AnimalTypes {

    @Id
    private Long id;

    @Column("type_name")
    private String typeName;
}
