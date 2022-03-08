package PetMyPet.images.files;

import PetMyPet.images.database.DBImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileService {

    public static Mono<DBImage> createFile(FilePart filePart, String folder, DBImage dbImage) {
        try {
            String fileName = dbImage.getPath();
            Long hotelId = dbImage.getHotelId();

            String fullPath = folder + "/" + fileName;
            Path path = Files.createFile(Paths.get(fullPath).toAbsolutePath().normalize());

            AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE); //TODO ugh
            DataBufferUtils.write(filePart.content(), channel, 0)
                    .doOnComplete(() -> {
                        try {
                            channel.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .subscribe();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Mono.just(dbImage);
    }

    public static boolean isAcceptable(String fileName){

        String extension = FilenameUtils.getExtension(fileName);

        return extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg");
    }

    public static String cleanName(String fileName){

        return fileName.replaceAll("[^A-Za-z0-9.]", "");
    }
}
