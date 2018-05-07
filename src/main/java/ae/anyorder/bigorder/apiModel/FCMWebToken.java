package ae.anyorder.bigorder.apiModel;

import lombok.Data;

/**
 * Created by Frank on 4/10/2018.
 */
@Data
public class FCMWebToken {
    private String webToken;
    private String browser;
    private String version;
    private Long userId;
}
