package ae.anyorder.bigorder.apiModel;

import ae.anyorder.bigorder.model.CartEntity;
import ae.anyorder.bigorder.model.StoreBrandEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Frank on 4/22/2018.
 */
@Data
public class CheckOut {
    private String currency;
    private StoreBrandEntity storeBrand;
    private List<CartEntity> carts;
    private BigDecimal grandTotal;
}
