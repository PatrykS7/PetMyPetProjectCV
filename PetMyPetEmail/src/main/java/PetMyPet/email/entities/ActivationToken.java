package PetMyPet.email.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("core.activation_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationToken implements Persistable<String> {

    @Id
    private String username;
    private String token;
    @Column("created_at")
    private LocalDateTime createdAt;

    @Override
    @JsonIgnore
    public String getId() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return true;
    }
}
