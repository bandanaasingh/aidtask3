package ae.anyorder.bigorder.util;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Frank on 4/3/2018.
 */
@Scope(value = "singleton")
@Component
public class MessageBundle {
    private static final Logger log = Logger.getLogger(MessageBundle.class);

    public static String getMessage(String key, String file) {
        try {
            Properties prop = readProperties(file);
            return prop.getProperty(key);
        } catch (Exception e) {
            log.error("Error occurred while reading file "+file, e);
        }
        return null;
    }

    public static String generateTokenString() {
        return UUID.randomUUID().toString();

    }

    public static Properties readProperties(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(fileName);

        Properties prop = new Properties();
        prop.load(input);

        return prop;
    }


    public static Timestamp getCurrentTimestampSQL(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getSenderEmail(){
        return System.getProperty("SENDER_EMAIL");
    }

    public static String getSenderPassword(){
        return System.getProperty("SENDER_PASSWORD");
    }

    public static int getSenderPort(){
        return Integer.parseInt(System.getProperty("SENDER_PORT"));
    }

    public static String getSenderHost(){
        return System.getProperty("SENDER_HOST");
    }

    public static String getHostName() {
        return System.getProperty("HOST");
    }

    public static boolean isLocalHost() {
        return getHostName().equals("localhost");
    }

    public static String getSecretKey(){
        return System.getProperty("SECRET_KEY");
    }

    public static String getSMSToken(){
        return System.getProperty("SMS_TOKEN");
    }

    public static String getSMSFrom(){
        return System.getProperty("SMS_FROM");
    }

    public static String getTwilioSid(){
        return System.getProperty("TWILIO_SMS_SID");
    }

    public static String getTwilioAuthToken(){
        return System.getProperty("TWILIO_SMS_TOKEN");
    }

    public static String getTwilioSMSFrom(){
        return System.getProperty("TWILIO_SMS_FROM");
    }

    public static String getGoogleMapApiKey(){
        return System.getProperty("GOOGLE_DISTANCE_API_KEY");
    }

    public static String getSupportNumber(){
        return System.getProperty("SUPPORT_NUMBER");
    }


    public static String getPropertyKey(String key, File file){
        try {
            Properties prop = readFileAsProperty(file);
            return prop.getProperty(key);
        } catch (Exception e) {
            log.error("Error occurred while reading file "+file,e);
        }

        return null;
    }

    public static Properties readFileAsProperty(File filename) throws IOException {
        InputStream stream = new FileInputStream(filename);
        Properties prop = new Properties();
        prop.load(stream);
        return prop;
    }
}
