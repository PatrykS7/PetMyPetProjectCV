package PetMyPet.images.service;

import PetMyPet.images.database.DBImage;
import PetMyPet.images.database.DBImageRepo;
import PetMyPet.images.files.FileTools;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {

    private final DBImageRepo dbImageRepo;

    @Autowired
    public ImageService(DBImageRepo dbImageRepo) {
        this.dbImageRepo = dbImageRepo;
    }

    public Flux<Void> saveImages(Long hotelId, Flux<FilePart> file) {

        return file.filter((f) -> FileTools.isAcceptable(f.filename())) //check extensions
                .flatMap(filePart -> FileTools.createFile( filePart,"images", new DBImage(hotelId, filePart.filename()))
                        .flatMap(dbImageRepo::save)
                            .then()
                            .onErrorResume(Exception.class, t ->
                                Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable to send files"))));
    }

    public Mono<byte[]> getImageByPath(String path){

        try{
            return Mono.just( FileUtils.readFileToByteArray(new File("images/" + path)) );
        }
        catch (Exception e){
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Files not found"));
        }
    }

    public Mono<byte[]> getResizedImage(String path, int width, int height) throws IOException {

        try {

            byte[] fileData = FileUtils.readFileToByteArray(new File("images/" + path));
            return Mono.just( FileTools.scale(fileData,width,height) );
        }
        catch (Exception e){
            return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Files not found"));
        }

    }

    public Mono<List<String>> getHotelPathsById(Long id) {

        return dbImageRepo.getPathByHotelId(id).collectList()
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found")));
    }

    public Mono<Void> deleteImage(String path){

        return dbImageRepo.deleteByPath(path)
                .doOnSuccess( unused -> {

                    Path filePath = Paths.get("images/" + path);
                    try {
                        Files.delete(filePath);
                    } catch (IOException e) {
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Files not found"));
                    }
                })
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Files not found")));
    }

    public Mono<Void> pickMain(String path) {

        Long hotelId = Long.parseLong(path.substring(0, path.indexOf("_")));

        return dbImageRepo.setMainImageNull(hotelId)
                .then(dbImageRepo.setMainImage(path))
                .onErrorResume(Exception.class, t ->
                        Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Unable change main image")));
    }

    public Mono<byte[]> getMainImageByHotelId(Long hotelId) {

        return dbImageRepo.getMainPathByHotelId(hotelId)
                .flatMap( (path) -> {

                    try {
                        return Mono.just( FileUtils.readFileToByteArray(new File("images/" + path)) );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));
                });
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
