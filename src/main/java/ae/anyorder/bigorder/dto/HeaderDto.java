package ae.anyorder.bigorder.dto;

import lombok.Data;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
public class HeaderDto {
    private String id;
    private String userId;
    private String username;
    private String password;
    private String accessToken;
    private String verificationCode;
    private String newPassword;
    private String merchantId;
    private String addressId;
    private Long facebookId;
    private String brandId;
    private String storeManagerId;
    private String cartId;
    private String orderId;
    private String storeId;
    private String driverId;
    private String customerAreaId;
}
