package PetMyPet.images.database;

import PetMyPet.images.files.FileTools;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("core.images")
@Data
@NoArgsConstructor
public class DBImage implements Persistable<Long> {

    @Id
    @Column("hotel_id")
    private Long hotelId;

    private String path;

    @Column("main_image")
    private Boolean mainImage;

    public DBImage(Long hotelId, String path) {

        this.hotelId = hotelId;

        String salt = UUID.randomUUID().toString();
        this.path = hotelId + "_" + salt +  FileTools.cleanName(path);
    }

    @Override
    @JsonIgnore
    public Long getId() {
        return hotelId;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return true;
    }
}
