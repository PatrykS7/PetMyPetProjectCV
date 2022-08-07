package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filters {

    private String location;
    private Integer range;
    private LocalDate startingDate;
    private LocalDate endingDate;
    private Long animalType;
}
