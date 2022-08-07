package PetMyPet.images.files;

import PetMyPet.images.database.DBImage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileTools {

    public static Mono<DBImage> createFile(FilePart filePart, String folder, DBImage dbImage) {
        try {
            String fileName = dbImage.getPath();

            String fullPath = folder + "/" + fileName;
            Path path = Files.createFile(Paths.get(fullPath).toAbsolutePath().normalize());

            AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE); //TODO ugh
            DataBufferUtils.write(filePart.content(), channel, 0)
                    .doOnComplete(() -> {
                        try {
                            channel.close();
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    })
                    .subscribe();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Mono.just(dbImage);
    }

    public static byte[] scale(byte[] fileData, int width, int height) throws IOException {

        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {

            BufferedImage img = ImageIO.read(in);
            if(height == 0) {
                height = (width * img.getHeight())/ img.getWidth();
            }
            if(width == 0) {
                width = (height * img.getWidth())/ img.getHeight();
            }
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public static boolean isAcceptable(String fileName){

        String extension = FilenameUtils.getExtension(fileName);

        return extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg");
    }

    public static String cleanName(String fileName){

        return fileName.replaceAll("[^A-Za-z0-9.]", "");
    }
}
