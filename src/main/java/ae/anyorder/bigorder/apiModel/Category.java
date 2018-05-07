package ae.anyorder.bigorder.apiModel;

import ae.anyorder.bigorder.enums.Status;
import lombok.Data;

/**
 * Created by Frank on 4/28/2018.
 */
@Data
public class Category {
    private String name;
    private Status status;
}
