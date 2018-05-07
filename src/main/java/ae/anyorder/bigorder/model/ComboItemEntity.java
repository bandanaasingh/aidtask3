package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.util.JsonTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity
@Table(name = "combo_item")
public class ComboItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    @Column(columnDefinition = "LONGTEXT NULL DEFAULT NULL")
    private String description;
    private String overview;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "min_person")
    private Integer minPerson;
    @Temporal(TemporalType.TIME)
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    @Column(name = "available_start_time")
    private Date availableStartTime;
    @Temporal(TemporalType.TIME)
    @JsonDeserialize(using = JsonTimeDeserializer.class)
    @Column(name = "available_end_time")
    private Date availableEndTime;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comboItem"})
    private StoreBrandEntity storeBrand;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comboItem"})
    private CategoryEntity category;
    @OneToMany(mappedBy = "comboItem")
    @JsonIgnoreProperties({"hibernateLazyInitializer",  "handler", "comboItem"})
    private List<ItemEntity> items;

    public String toString(){
        return null;
    }
}
