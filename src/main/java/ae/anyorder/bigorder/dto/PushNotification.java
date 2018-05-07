package ae.anyorder.bigorder.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 4/3/2018.
 */
@Data
public class PushNotification {
    private Map<String, String> message;
    private List<String> tokens;
}
