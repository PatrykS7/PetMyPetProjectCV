package PetMyPet.images.controllers;

import PetMyPet.images.database.DBImage;
import PetMyPet.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
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

    @GetMapping("/getHotelPathsById/{hotelId}")
    public Mono<List<String>> getHotelPathsById(@PathVariable Long hotelId){

        return imageService.getHotelPathsById(hotelId);
    }
}
