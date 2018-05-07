package ae.anyorder.bigorder.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Frank on 4/9/2018.
 */
@Data
@Entity
@Table(name = "preferences")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PreferencesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pref_key", unique = true)
    private String prefKey;
    private String value;
    @Column(name = "pref_title")
    private String prefTitle;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("preference")
    //@JsonManagedReference
    private PreferenceSectionEntity section;

    /*public String toString(){
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreferencesEntity that = (PreferencesEntity) o;
        return Objects.equals(getId(), that.getPrefKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrefKey());
    }*/
}
