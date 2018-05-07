package ae.anyorder.bigorder.model;

import ae.anyorder.bigorder.enums.PaymentMode;
import ae.anyorder.bigorder.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
@Entity(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date", columnDefinition="TIMESTAMP NULL DEFAULT NULL")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp createdDate;

    @Column(name = "delivery_date")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp deliveryDate;
    @Column(name = "delivered_date")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp deliveredDate;
    @Column(name = "grand_total")
    private BigDecimal grandTotal;
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;
    private String note;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private ClientEntity client;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private StoreEntity store;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private AddressEntity address;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderComboItemEntity> orderComboItems;


    public String toString(){
        return null;
    }
}
