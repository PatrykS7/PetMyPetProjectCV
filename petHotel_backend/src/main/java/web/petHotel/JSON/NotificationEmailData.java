package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEmailData {

    private String recipient;
    private String hotelName;
    private String animalTypeStr;
    private String animalName;
    private LocalDate checkIn;
    private LocalDate checkOut;

    private String status;
}
