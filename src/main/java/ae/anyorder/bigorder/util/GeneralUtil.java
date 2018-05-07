package ae.anyorder.bigorder.util;

import ae.anyorder.bigorder.dto.ErrorMessage;
import ae.anyorder.bigorder.dto.HeaderDto;
import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.exception.MyException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Frank on 4/5/2018.
 */
@Scope(value = "singleton")
@Component
public class GeneralUtil {
    private static Logger log = Logger.getLogger(GeneralUtil.class);

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String VERIFICATION_CODE = "verificationCode";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String ADDRESS_ID = "addressId";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String MERCHANT_ID = "merchantId";
    public static final String FACEBOOK_ID = "facebookId";
    public static final String BRAND_ID = "brandId";
    public static final String STORE_MANAGER_ID = "storeManagerId";
    public static final String CART_ID = "cartId";
    public static final String ORDER_ID = "orderId";
    public static final String STORE_ID = "storeId";
    public static final String DRIVER_ID = "driverId";
    public static final String AREA_ID = "areaId";
    public static final String CUSTOMER_AREA_ID = "customerAreaId";


    public void logError(Logger log, String message, Exception e) {
        if (e instanceof MyException)
            log.info(e.getMessage());
        else
            log.error(message, e);
    }

    public void wait(int millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            log.error("Error occurred while holding a thread", e);
        }
    }

    public static void fillHeaderCredential(HttpHeaders headers, HeaderDto headerDto, String... expectedFields) throws Exception {
        for (String field : expectedFields) {
            switch (field){
                case USERNAME:
                    headerDto.setUsername(extractHeader(headers, USERNAME));
                    break;
                case PASSWORD:
                    headerDto.setPassword(extractHeader(headers, PASSWORD));
                    break;
                case VERIFICATION_CODE:
                    headerDto.setVerificationCode(extractHeader(headers, VERIFICATION_CODE));
                    break;
                case NEW_PASSWORD:
                    headerDto.setNewPassword(extractHeader(headers, NEW_PASSWORD));
                    break;
                case ID:
                    headerDto.setId(extractHeader(headers, ID));
                    break;
                case USER_ID:
                    headerDto.setUserId(extractHeader(headers, USER_ID));
                    break;
                case BRAND_ID:
                    if(headers.get(BRAND_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, BRAND_ID));
                    break;
                case STORE_ID:
                    if(headers.get(BRAND_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, BRAND_ID));
                    break;
                case CART_ID:
                    if(headers.get(CART_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, CART_ID));
                    break;
                case ORDER_ID:
                    if(headers.get(ORDER_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, ORDER_ID));
                    break;
                case ADDRESS_ID:
                    if(headers.get(ADDRESS_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, ADDRESS_ID));
                    break;
                case ACCESS_TOKEN:
                    if(headers.get(ACCESS_TOKEN) != null)
                        headerDto.setBrandId(extractHeader(headers, CART_ID));
                    break;
                case FACEBOOK_ID:
                    if(headers.get(FACEBOOK_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, FACEBOOK_ID));
                    break;
                case DRIVER_ID:
                    if(headers.get(DRIVER_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, DRIVER_ID));
                    break;
                case CUSTOMER_AREA_ID:
                    if(headers.get(CUSTOMER_AREA_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, CUSTOMER_AREA_ID));
                    break;
                case AREA_ID:
                    if(headers.get(AREA_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, AREA_ID));
                    break;
                case STORE_MANAGER_ID:
                    if(headers.get(STORE_MANAGER_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, STORE_MANAGER_ID));
                    break;
                case MERCHANT_ID:
                    if(headers.get(MERCHANT_ID) != null)
                        headerDto.setBrandId(extractHeader(headers, MERCHANT_ID));
                    break;
            }
        }
    }

    public static String extractHeader(HttpHeaders headers, String field) throws Exception {
        List<String> hd = headers.get(field);
        if (hd != null && hd.size() > 0)
            return hd.get(0);
        else
            throw new MyException("VLD001");
    }

    public static String encryptPassword(String password) {
        if (password != null && !password.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.encode(password);
        }
        return null;
    }

    public static Boolean matchDBPassword(String rawPassword, String dbEncryptedPassword) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(rawPassword, dbEncryptedPassword))
            throw new MyException("PASS002");

        return true;
    }
    public static String separateString(String separator, String... args){
        StringBuilder builder = new StringBuilder();

        for(String arg: args)
            builder.append(arg+separator);
        return builder.toString();
    }

    public ErrorMessage generateError(Exception e) {
        String statusCode = "ERR001";
        String message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        if(e instanceof MyException) {
            statusCode = ((MyException) e).getErrorCode();
            message = e.getMessage();
        }  else if(e instanceof GeneralSecurityException) {
            statusCode = "ERR002";
            message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        } else if(e instanceof InvocationTargetException) {
            statusCode = "ERR003";
            message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        } else if (e instanceof  NoSuchMethodException) {
            statusCode = "ERR004";
            message = MessageBundle.getMessage(statusCode, "errorCodes.properties");
        }else if (e instanceof SQLException) {
            statusCode = "ERR006";
            message = e.getMessage();
        } else if (e instanceof Exception) {
            statusCode = "ERR005";
            message = e.getMessage();
        }
        ErrorMessage error = new ErrorMessage();
        error.setSuccess(Boolean.FALSE);
        error.setCode(statusCode);
        error.setMessage(message);
        return error;
    }
}

