package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "store_brand")
@DynamicUpdate
@DynamicInsert
public class StoreBrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String logo;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(columnDefinition = "LONGTEXT NULL DEFAULT NULL")
    private String description;
    @Column(name = "serving_distance")
    private BigDecimal servingDistance;
    @Column(name = "min_order")
    private BigDecimal minOrderAmount;
    @Column(name= "order_before")
    private Integer placeOrderBefore;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comboItem"})
    private MerchantEntity merchant;
    @OneToMany(mappedBy = "storeBrand", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("comboItem")
    private List<StoreEntity> stores;
    @OneToMany(mappedBy = "storeBrand")
    @JsonIgnoreProperties("comboItem")
    private List<ComboItemEntity> comboItems;

    public String toString(){
        return null;
    }

}
