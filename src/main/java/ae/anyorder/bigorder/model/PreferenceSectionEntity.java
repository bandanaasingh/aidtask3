package ae.anyorder.bigorder.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

/**
 * Created by Frank on 4/9/2018.
 */
@Data
@Entity
@Table(name = "preference_section")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PreferenceSectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String section;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("section")
    //@JsonManagedReference
    private PreferenceTypeEntity group;
    @OneToMany(mappedBy = "section")
    //@JsonBackReference
    @JsonIgnoreProperties("group")
    private List<PreferencesEntity> preference;

    /*public String toString(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreferenceSectionEntity that = (PreferenceSectionEntity) o;
        return Objects.equals(getId(), that.getSection());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSection());
    }*/
}
