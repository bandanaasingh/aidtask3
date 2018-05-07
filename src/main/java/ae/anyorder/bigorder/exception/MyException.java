package ae.anyorder.bigorder.exception;

import ae.anyorder.bigorder.util.MessageBundle;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Frank on 4/3/2018.
 */
@Data
public class MyException extends RuntimeException{
    public String errorCode;
    public String message;

    @Autowired
    MessageBundle messageBundle;

    public MyException(String errorCode, String exceptionCause) {
        super(errorCode);
        this.errorCode=errorCode;
        this.message = messageBundle.getMessage(errorCode,"errorCodes.properties") + ". "+exceptionCause;
    }

    public MyException(String errorCode, Throwable e) {
        super(errorCode, e);
        this.errorCode=errorCode;
        this.message=e.getLocalizedMessage();
    }

    public MyException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.message = messageBundle.getMessage(errorCode, "errorCodes.properties");
    }
}
