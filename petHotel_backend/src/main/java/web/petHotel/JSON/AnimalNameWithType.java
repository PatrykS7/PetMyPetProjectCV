package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalNameWithType {

    private String name;
    @Column("type_name")
    private String typeName;
    private String owner;
}
