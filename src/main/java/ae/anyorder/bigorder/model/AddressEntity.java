package ae.anyorder.bigorder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "delivery_address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String latitude;
    private String longitude;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ClientEntity customer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AreaEntity area;

    public String toString(){
        return null;
    }
}
