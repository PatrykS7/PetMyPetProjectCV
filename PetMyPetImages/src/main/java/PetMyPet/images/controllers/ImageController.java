package PetMyPet.images.controllers;

import PetMyPet.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.ws.rs.Path;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping(
            path = "/save/{hotelId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public  Flux<Void> saveImages(@PathVariable Long hotelId, @RequestPart("File") Flux<FilePart> file){

        return imageService.saveImages(hotelId, file);
    }

    @GetMapping(
            path = "/getImageByPath/{path}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public Mono<byte[]> getImageByPath(@PathVariable String path) throws IOException {

        return imageService.getImageByPath(path);
    }

    @GetMapping(
            path = "/getResizedImageByPath/{path}/",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public Mono<byte[]> getResizedImageByPath(@PathVariable String path, @RequestParam int width, @RequestParam int height) throws IOException {

        return imageService.getResizedImage(path, width, height);
    }

    @GetMapping("/getHotelPathsById/{hotelId}")
    public Mono<List<String>> getHotelPathsById(@PathVariable Long hotelId){

        return imageService.getHotelPathsById(hotelId);
    }

    @DeleteMapping("/delete/{path}")
    public Mono<Void> deleteImage(@PathVariable String path){

        return imageService.deleteImage(path);
    }

    @PostMapping("/pickMain/{path}")
    public Mono<Void> pickMain(@PathVariable String path){

        return imageService.pickMain(path);
    }

    @GetMapping(
            path = "/getMain/{hotelId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public Mono<byte[]> getMainImageByHotelId(@PathVariable Long hotelId) throws IOException {

        return imageService.getMainImageByHotelId(hotelId);
    }
}
