package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.petHotel.entities.Animal;
import web.petHotel.entities.Hotel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithDataWithPhoneNum {

    private Long id;
    private Hotel hotel;
    private Animal animal;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;
    private String ownerPhoneNumber;
}
