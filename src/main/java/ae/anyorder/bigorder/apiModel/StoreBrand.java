package ae.anyorder.bigorder.apiModel;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Frank on 4/15/2018.
 */
@Data
public class StoreBrand {
    private String name;
    private String logo;
    private String description;
    private BigDecimal servingDistance;
    private BigDecimal minOrderAmount;
    private Integer orderPlaceBefore;
    private List<Store> stores;

    @Data
    public static class Store{
        private Long id;
        private Long areaId;
        private String latitude;
        private String contactNo;
        private String longitude;
        private String addressNote;
        private String givenLocation;
    }
}
