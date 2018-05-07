package ae.anyorder.bigorder.util;

import ae.anyorder.bigorder.exception.MyException;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by Frank on 4/3/2018.
 */
@Scope(value = "singleton")
@Component
public class TwilioSMSUtil{
    private static final Logger log = Logger.getLogger(TwilioSMSUtil.class);
    @Autowired
    MessageBundle messageBundle;

    private final String ACCOUNT_SID = messageBundle.getTwilioSid();
    private final String AUTH_TOKEN = messageBundle.getTwilioAuthToken();
    private final String FROM = messageBundle.getTwilioSMSFrom();

    public void sendSMS(String countryCode, String to, String textMessage) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {
            Message message = Message
                    .creator(new PhoneNumber(countryCode + to), // to
                            new PhoneNumber(FROM), // from
                            textMessage)
                    .create();
            log.info(message.getSid());
        } catch (TwilioException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new MyException("TWI001");
        }
    }
}
