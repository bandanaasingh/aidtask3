package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer quantity;
    private String description;
    private Status status;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_item_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comboItem"})
    private ComboItemEntity comboItem;

    public String toString(){
        return null;
    }
}
