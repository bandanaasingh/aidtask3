package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "store")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "created_date")
    private Timestamp createdDate;
    private String contactNo;
    private String latitude;
    private String longitude;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "address_note")
    private String addressNote;
    @Column(name = "given_location")
    private String givenLocation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "store_brand_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "stores"})
    private StoreBrandEntity storeBrand;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AreaEntity area;

    @OneToMany(mappedBy = "store", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<StoreAreaEntity> storesAreas;

    public String toString(){
        return null;
    }
}
