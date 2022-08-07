package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("core.users")
public class User implements Persistable<String> {

    @Id
    private String username;
    private String password;
    private boolean enabled = false;
    private String authority;

    public User(String username, String password, boolean enabled, String authority) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authority = authority;
    }

    @JsonIgnore
    @Override
    public String getId() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return true;
    }
}
