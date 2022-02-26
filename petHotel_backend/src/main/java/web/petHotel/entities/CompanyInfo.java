package web.petHotel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("core.company_info")
public class CompanyInfo implements Persistable<String> {

    @Id
    private String username;
    private String name;
    private String nip;
    private String regon;

    @JsonIgnore
    @Transient
    private boolean isNew = true;

    public CompanyInfo(String username, String name, String nip, String regon) {
        this.username = username;
        this.name = name;
        this.nip = nip;
        this.regon = regon;
    }

    public CompanyInfo(String username, String name, String nip, String regon, boolean isNew) {
        this.username = username;
        this.name = name;
        this.nip = nip;
        this.regon = regon;
        this.isNew = isNew;
    }

    @JsonIgnore
    @Transient
    @Override
    public String getId() {
        return username;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
