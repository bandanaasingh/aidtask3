package ae.anyorder.bigorder.dto;

import lombok.Data;

/**
 * Created by Frank on 4/10/2018.
 */
@Data
public class ErrorMessage {
    private Boolean success;
    private String code;
    private String message;
}
