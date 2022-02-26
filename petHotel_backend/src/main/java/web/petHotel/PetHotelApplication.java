package web.petHotel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
@OpenAPIDefinition(info = @Info(
        title = "petHotel",
        description = "petHotel backend reactive API"
))
public class PetHotelApplication {

    @Bean
    @LoadBalanced
    WebClient.Builder getWebClientBuilder(){
        return WebClient.builder();
    }

    public static void main(String[] args) {
        SpringApplication.run(PetHotelApplication.class, args);
    }

}
