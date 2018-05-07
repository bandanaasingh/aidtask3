package ae.anyorder.bigorder.util;

import ae.anyorder.bigorder.dto.PushNotification;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Frank on 4/3/2018.
 */
@Scope(value = "singleton")
@Component
public class FCMNotificationUtil {
    private static final Logger log = Logger.getLogger(FCMNotificationUtil.class);

    public final static String AUTH_KEY_FCM = "AIzaSyA6aSnn2iJXdjcLfsW49GIdM8bc5mpq4ps";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public static RequestBody body;
    public static OkHttpClient client = new OkHttpClient();

    public static void sendFCMPushNotification(PushNotification pushNotification) {
        try {
            MediaType mediaType = MediaType.parse("application/json");
            JSONObject obj = new JSONObject();
            JSONObject msgObject = new JSONObject();
            msgObject.put("priority", "high");

            if(pushNotification.getMessage()!=null) {
                for (Map.Entry<String, String> entry : pushNotification.getMessage().entrySet()) {
                    msgObject.put(entry.getKey(), entry.getValue());
                }
            }

            for(String token: pushNotification.getTokens()) {
                log.info("Token: " + token);
                obj.put("to", token);
                obj.put("notification", msgObject);
                body = RequestBody.create(mediaType, obj.toString());
                Request request = new Request.Builder().url(API_URL_FCM).post(body)
                        .addHeader("content-type", "application/json")
                        .addHeader("authorization", "key=" + AUTH_KEY_FCM).build();

                Response response = client.newCall(request).execute();
                log.info("PushNotification response: " + response.body().string());
            }
        } catch (Exception e) {}
    }
}
