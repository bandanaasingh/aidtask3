package ae.anyorder.bigorder.apiModel;

import ae.anyorder.bigorder.enums.Status;
import lombok.Data;

/**
 * Created by Frank on 4/10/2018.
 */
@Data
public class Area {
    private String name;
    private String latitude;
    private String longitude;
    private String street;
    private Status status;
    private Long parentId;
}
