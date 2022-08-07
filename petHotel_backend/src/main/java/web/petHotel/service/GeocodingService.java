package web.petHotel.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;;

@Data
@NoArgsConstructor
public class GeocodingService {
    private Double lat;
    private Double lng;

    private String API_KEY = "AIzaSyBbcvEi29n2B0eEX3bUJH5R7w7cmTiltFI";

    public GeocodingService(String address) throws IOException, InterruptedException, ApiException {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        this.lat = Double.valueOf(gson.toJson(results[0].geometry.location.lat));
        this.lng = Double.valueOf(gson.toJson(results[0].geometry.location.lng));

        context.shutdown();
    }

    public void geocodingAddressService(String address) throws ExecutionException, InterruptedException {

        address = "Trouń Bema 40";

        WebClient.Builder webClient = WebClient.builder();

         var response = webClient.build()
                .get()
                .uri("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + API_KEY)
                        .retrieve()
                                .bodyToMono(String.class).toFuture().get();

        System.out.println(response);
    }
}
