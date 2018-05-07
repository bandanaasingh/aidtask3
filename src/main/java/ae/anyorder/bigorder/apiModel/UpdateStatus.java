package ae.anyorder.bigorder.apiModel;

import ae.anyorder.bigorder.enums.Status;
import lombok.Data;

/**
 * Created by Frank on 4/26/2018.
 */
@Data
public class UpdateStatus {
    private Status status;
}
