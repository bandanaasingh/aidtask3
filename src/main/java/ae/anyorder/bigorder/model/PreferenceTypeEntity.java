package ae.anyorder.bigorder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by Frank on 4/9/2018.
 */
@Data
@Entity
@Table(name = "preference_type")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PreferenceTypeEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    @OneToMany(mappedBy = "group")
    //@JsonBackReference
    @JsonIgnoreProperties("group")
    private List<PreferenceSectionEntity> section;

   /* public String toString(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreferenceTypeEntity that = (PreferenceTypeEntity) o;
        return Objects.equals(getId(), that.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroupName());
    }*/
}
