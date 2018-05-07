package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "area")
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate(true)
public class AreaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Status status;
    private String street;
    private String latitude;
    private String longitude;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AreaEntity parent;
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AreaEntity> child;

    public String toString(){
        return null;
    }
}
