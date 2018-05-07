package ae.anyorder.bigorder.apiModel;

import lombok.Data;

/**
 * Created by Frank on 4/22/2018.
 */
@Data
public class Cart {
    private Long storeId;
    private Long clientId;
    private Long comboItemId;
    private Integer quantity;
}
