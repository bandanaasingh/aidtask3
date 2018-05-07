package ae.anyorder.bigorder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Frank on 4/3/2018.
 */
@Data
@Entity(name = "merchant")
public class MerchantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String businessTitle;
    private String url;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "merchant"})
    private UserEntity user;
    @OneToMany(mappedBy = "merchant")
    @JsonIgnoreProperties("comboItem")
    private List<StoreBrandEntity> storeBrand;

    public String toString(){
        return null;
    }
}