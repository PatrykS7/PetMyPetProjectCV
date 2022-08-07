package web.petHotel.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelIdWithPriceMap {

    private Long hotelId;

    private Map<Long, Float> prices = new HashMap<>();
}
