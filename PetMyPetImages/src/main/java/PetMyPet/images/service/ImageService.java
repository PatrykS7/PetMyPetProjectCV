package PetMyPet.images.service;

import PetMyPet.images.database.DBImage;
import PetMyPet.images.database.DBImageRepo;
import PetMyPet.images.files.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ImageService {

    private final DBImageRepo dbImageRepo;

    @Autowired
    public ImageService(DBImageRepo dbImageRepo) {
        this.dbImageRepo = dbImageRepo;
    }

    public Flux<Void> saveImages(Long hotelId, Flux<FilePart> file) {

        return file.filter((f) -> FileService.isAcceptable(f.filename())) //check extensions
                .flatMap(filePart -> FileService.createFile( filePart,"images", new DBImage(hotelId, filePart.filename()))
                        .flatMap(dbImageRepo::save)
                            .then()
                            .onErrorResume(Exception.class, t ->
                                Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to send files"))));
    }

    public Mono<byte[]> getImageByPath(String path) throws IOException {

        return Mono.just( FileUtils.readFileToByteArray(new File("images/" + path)) );
    }

    public Mono<List<String>> getHotelPathsById(Long id) {

        return dbImageRepo.getPathByHotelId(id).collectList();
    }

//    public Flux<FileSystemResource> getImagesByHotelId(Long hotelId) {
//
//        return dbImageRepo.getPathByHotelId(hotelId)
//                .map( path -> {
//                    return new FileSystemResource( "images/" + path);
//                });

//        Flux<List<String>> listMono = dbImageRepo.getPathByHotelId(hotelId).collectList().flatMapMany(Flux::just);
//
//        return listMono
//                .flatMap( list -> {
//
//                    List<FileSystemResource> fileServices = new ArrayList<>();
//                    list.forEach( li -> {
//                        fileServices.add(new FileSystemResource("images/" + li));
//                    });
//
//                    return Flux.fromIterable(fileServices);
//                });
//    }
}
