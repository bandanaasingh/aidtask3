package ae.anyorder.bigorder.util;

import ae.anyorder.bigorder.dto.ErrorMessage;
import ae.anyorder.bigorder.exception.MyException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 4/7/2018.
 */
@Data
public class ServiceResponse {
    private Boolean success;
    private String message;
    private Object params;

    /*@Autowired
    MessageBundle messageBundle;*/

    public ServiceResponse() {
    }

    public ServiceResponse(String message) {
        this(true, message);
    }

    public ServiceResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public HttpHeaders generateApplicationErrors(MyException e){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("errorCode", e.getErrorCode());
        httpHeaders.add("errorMessage", MessageBundle.getMessage(e.errorCode, "errorCodes.properties"));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    /*public static HttpHeaders generateRuntimeErrors(Exception e) {
        String statusCode = "MERR001";
        String message = MessageBundle.getMessage(statusCode, "errorCodes.properties");

        if(e instanceof MyException) {
            statusCode = ((MyException) e).getErrorCode();
            message = e.getMessage();
        }  else if(e instanceof GeneralSecurityException) {
            statusCode = "MERR002";
            message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        } else if(e instanceof InvocationTargetException) {
            statusCode = "MERR003";
            message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        } else if (e instanceof  NoSuchMethodException) {
            statusCode = "MERR004";
            message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        }else if (e instanceof SQLException) {
            statusCode = "MERR006";
            message = e.getMessage();
        } else if (e instanceof Exception) {
            //General Exception During Development Period Only
            //In Production period this catch block should be commented
            statusCode = "MERR005";
            message = e.getMessage();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("errorCode", statusCode);
        httpHeaders.add("errorMessage", message);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }*/

}
