package ae.anyorder.bigorder.apiModel;

import lombok.Data;

/**
 * Created by Frank on 4/29/2018.
 */
@Data
public class Items {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
}
