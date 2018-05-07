package ae.anyorder.bigorder.apiModel;

import ae.anyorder.bigorder.enums.PaymentMode;
import lombok.Data;

/**
 * Created by Frank on 4/22/2018.
 */
@Data
public class Order {
    private Long clientId;
    private Long addressId;
    private Long storeBrandId;
    private PaymentMode paymentMode;
    private String note;
}
