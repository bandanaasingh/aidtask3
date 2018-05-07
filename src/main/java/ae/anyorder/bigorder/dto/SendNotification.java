package ae.anyorder.bigorder.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Frank on 4/15/2018.
 */
@Data
public class SendNotification {
    private Timestamp validTill;
    private String message;
    private List<Integer> userIds;

}
