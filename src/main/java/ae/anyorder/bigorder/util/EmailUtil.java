package ae.anyorder.bigorder.util;

import ae.anyorder.bigorder.dto.Constants;
import ae.anyorder.bigorder.enums.PreferenceType;
import ae.anyorder.bigorder.enums.Role;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.model.StoreEntity;
import ae.anyorder.bigorder.model.UserEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;

/**
 * Created by Frank on 4/9/2018.
 */

@Component
@Scope(value = "singleton")
public class EmailUtil {
    public static final Logger log = Logger.getLogger(EmailUtil.class);

    private static SimpleMailMessage message=null;
    private static String appName;
    private static String SYSTEM_PREF_FILE = "system_pref.properties";
    private static StringBuilder style = new StringBuilder();

    @Autowired
    JavaMailSender emailSender;

    /*@Autowired
    TemplateEngine templateEngine;*/

    /*public void sendHTMLEmail(String to, String subject, String templateName, Context context){
        *//*String body = templateEngine.process(templateName, context);
        try {
            MimeMessage mail = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(mail);
        }catch (Exception e){
            log.error("Error sending email. "+e.getMessage());
        }*//*
    }*/

    {
        try {
            appName = readPrefValue(PreferenceType.APPLICATION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        style.append("body{font-size: 16px; font-family: Roboto;}");
    }

    private static String readPrefValue(PreferenceType preferenceTypeType) throws Exception {
        Resource resource = new ClassPathResource(SYSTEM_PREF_FILE);
        return MessageBundle.getPropertyKey(preferenceTypeType.toString(), resource.getFile());
    }

    public static String createPasswordForNewUser(String url, String userName, String userEmail, String serverUrl) {
        StringBuilder body = new StringBuilder();
        body.append(getEmailHead(serverUrl));
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#f6f5f5' align='center' border='0' cellpadding='0' cellspacing='0' width='91.42857142857143%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='3' align='left'><h1 style='font-weight: normal; font-size: 25px; padding-top: 20px; margin-bottom: 10px;'>Dear " + userName + "</h1></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p>Your account details are as follows:</p></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#ffffff' align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='border-collapse: collapse; border-width: 1px; border-style: solid; border-color: #dcdcdd; border-left-width: 3px; border-left-color: #5c6d86; border-left-style: solid; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='1' style='padding-left: 15px; padding-top: 15px; width: 110px;'> <strong>USERNAME</strong> </td>");
        body.append("<td align='left' colspan='1' style='padding-top: 15px;'> <strong>" + userEmail + "</strong> </td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td colspan='2' style='padding-left: 15px; padding-bottom: 15px;'>");
        body.append("<p style='padding-bottom: 5px; margin-bottom: 0;'>Please click on the link to verify your account & create your own password. </p>");
        body.append("<a href='" + url + "' target='_blank' style='color: #2693ff; text-decoration: none;'>Verify Your Account</a>");
        body.append("</td>");
        body.append("</tr>");
        body.append("</table>");
        body.append("</td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td>");
        body.append("<p>If the above link does not work, please copy & paste the following URL in your browser.</p>");
        body.append("<p style='color: #5a5a5a; font-weight: 100;'>" + url + "</p>");
        body.append("</td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p style='padding-top: 10px; padding-bottom: 10px;'>Your account will be activated after verification.</p></td>");
        body.append("</tr>");
        body.append(getEmailFooter(serverUrl));
        return body.toString();
    }

    private static String getEmailHead(String serverUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append("<table align='center' border='0' cellpadding='0' cellspacing='0' width='100%' height='100%' style='color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        builder.append("<tr>");
        builder.append("<td>");
        builder.append("<table bgcolor='#f5f4f4' align='center' border='0' cellpadding='0' cellspacing='0' width='700px' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        builder.append("<tr>");
        builder.append("<td>");
        builder.append("<table bgcolor='#5c6d86' align='center' border='0' cellpadding='0' cellspacing='0' width='100%' height='82px' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        builder.append("<tr>");
        builder.append("<td colspan='3' align='center' style='padding-top: 15px; padding-bottom: 10px;'>");
        builder.append("<img src='" + serverUrl + "/resources/custom/images/logo.png' alt='" + Constants.APP_NAME + " Logo' style='position: relative; top: 45px;'>");
        builder.append("</td>");
        builder.append("</tr>");
        builder.append("</table>");
        builder.append("</td>");
        builder.append("</tr>");
        return builder.toString();
    }

    private static String getEmailFooter(String serverUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append("<tr>");
        builder.append("<td>");
        builder.append("<p style='padding-bottom: 5px; margin: 0;'>Sincerely, </p>");
        builder.append("<p style='color: #5c6d86; font-size: 18px; padding-bottom: 3px; margin: 0;'> <strong>"+ Constants.APP_NAME+" Team</strong> </p>");
        builder.append("<p style='padding-bottom: 50px; margin: 0; font-weight: 100;'> On demand delivery from every store of city</p>");
        builder.append("</td>");
        builder.append("</tr>");
        builder.append("</table>");
        builder.append("</td>");
        builder.append("</tr>");
        builder.append("<tr>");
        builder.append("<td>");
        builder.append("<table bgcolor='#5c6d86' align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        builder.append("<tr>");
        builder.append("<td colspan='1' style='width: 40.57142857142857%; height: 250px; padding-left: 50px;'><span style='color: #ffffff;font-size: 30px;'>"+ Constants.APP_NAME+" AVAILABLE In</span></td>");
        builder.append("<td colspan='1' style='width: 22.857142857142858%;'><a href='#'><img src='" + serverUrl + "/resources/custom/images/app_store.png' alt='app store'></a></td>");
        builder.append("<td colspan='1' style='width: 21.428571428571427%;'><a href='#'><img src='" + serverUrl + "/resources/custom/images/play_store.png' alt='play store'></a></td>");
        builder.append("</tr>");
        builder.append("</table>");
        builder.append("</td>");
        builder.append("</tr>");
        builder.append("</table>");
        builder.append("</td>");
        builder.append("</tr>");
        builder.append("<tfoot>");
        builder.append("<tr></tr>");
        builder.append("</tfoot>");
        builder.append("</table>");
        return builder.toString();
    }

    public static String sendInvoiceEmail(StoreEntity storeEntity, String fromDate, String toDate, String serverUrl) {
        StringBuilder body = new StringBuilder();
        body.append(getEmailHead(serverUrl));
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#f6f5f5' align='center' border='0' cellpadding='0' cellspacing='0' width='91.42857142857143%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='3' align='left'><h1 style='font-weight: normal; font-size: 25px; padding-top: 20px; margin-bottom: 10px;'>Dear " + storeEntity.getStoreBrand().getMerchant().getUser().getFirstName() + " " + storeEntity.getStoreBrand().getMerchant().getUser().getLastName() + "</h1></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p>We have sent you invoice for store: " + storeEntity.getStoreBrand().getName() + "(" + storeEntity.getArea().getParent()!=null? storeEntity.getArea().getParent().getName()+ ", ":null  + storeEntity.getArea().getName() + ") " + fromDate + " to " + toDate + "</p></td>");
        body.append("</tr>");
        body.append(getEmailFooter(serverUrl));
        return body.toString();
    }

    public static String notificationEmailUserStatus(UserEntity userEntity, String serverUrl) {
        StringBuilder body = new StringBuilder();
        body.append(getEmailHead(serverUrl));
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#f6f5f5' align='center' border='0' cellpadding='0' cellspacing='0' width='91.42857142857143%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='3' align='left'><h1 style='font-weight: normal; font-size: 25px; padding-top: 20px; margin-bottom: 10px;'>Dear " + userEntity.getFirstName() + " " + userEntity.getLastName() + "</h1></td>");
        body.append("</tr>");
        if (userEntity.getStatus().equals(Status.ACTIVE)) {
            body.append("<tr><td><p>Your account(" + userEntity.getUsername() + ") has been  re-activated.</p></td></tr>");
            if (userEntity.getRole().equals(Role.ROLE_MERCHANT)) {
                body.append("<tr><td><p>You can now start uploading your stores, product and services.</p></td></tr>");
            } else {
                body.append("<tr><td><p>You can now start using your account.</p></td></tr>");
            }
            body.append("<tr><td><p><a href='" + serverUrl + "' target='_blank' style='color: #2693ff; text-decoration: none;'>Login to "+ Constants.APP_NAME+" Support</a></p></td></tr>");
        } else if (userEntity.getStatus().equals(Status.INACTIVE)) {
            body.append("<tr><td><p>We have deactivated your account(" + userEntity.getUsername() + ")  as requested.</p></td></tr>");
            body.append("<tr><td><p><b>We look forward to seeing you back on "+Constants.SUPPORT_NAME+" Support soon.</b></p></td></tr>");
        }
        body.append(getEmailFooter(serverUrl));
        return body.toString();
    }

    public void sendEmail(String to, String subject, String text) {
        if (message == null)
            message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public String getAppName() {
        return appName;
    }

    public String welcomeEmailForNewUser(String userName, String userEmail, String serverUrl) {
        StringBuilder body = new StringBuilder();
        body.append(getEmailHead(serverUrl));
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#f6f5f5' align='center' border='0' cellpadding='0' cellspacing='0' width='91.42857142857143%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='3' align='left'><h1 style='font-weight: normal; font-size: 25px; padding-top: 20px; margin-bottom: 10px;'>Dear " + userName + "</h1></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p>Your account details are as follows:</p></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#ffffff' align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='border-collapse: collapse; border-width: 1px; border-style: solid; border-color: #dcdcdd; border-left-width: 3px; border-left-color: #5c6d86; border-left-style: solid; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='1' style='padding-left: 15px; padding-top: 15px; width: 110px;'> <strong>USERNAME</strong> </td>");
        body.append("<td align='left' colspan='1' style='padding-top: 15px;'> <strong>" + userEmail + "</strong> </td>");
        body.append("</tr>");
        body.append("</table>");
        body.append("</td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p style='padding-top: 10px; padding-bottom: 10px;'>Your account has been activated.</p></td>");
        body.append("</tr>");
        body.append(getEmailFooter(serverUrl));
        return body.toString();
    }

    public String resetForgotPassword(String url, String userName, String serverUrl) {
        StringBuilder body = new StringBuilder();
        body.append(getEmailHead(serverUrl));
        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#f6f5f5' align='center' border='0' cellpadding='0' cellspacing='0' width='91.42857142857143%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='3' align='left'><h1 style='font-weight: normal; font-size: 25px; padding-top: 20px; margin-bottom: 10px;'>Dear " + userName + "</h1></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p>Please click on the link below and navigate to reset your password.</p></td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td colspan='2' style='padding-left: 15px; padding-bottom: 15px;'>");
        body.append("<a href='" + url + "' target='_blank' style='color: #2693ff; text-decoration: none;'>Reset Password</a>");
        body.append("</td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td>");
        body.append("<p>If the above link does not work, please copy & paste the following URL in your browser.</p>");
        body.append("<p style='color: #5a5a5a; font-weight: 100;'>" + url + "</p>");
        body.append("</td>");
        body.append("</tr>");
        body.append("<tr>");
        body.append("<td><p style='padding-top: 10px; padding-bottom: 10px;'>Your account will be activated after verification.</p></td>");
        body.append("</tr>");
        body.append(getEmailFooter(serverUrl));
        return body.toString();
    }

    public String notificationEmailStoreStatus(String storeName, UserEntity userEntity, Status status, String serverUrl) {

        StringBuilder body = new StringBuilder();

        body.append(getEmailHead(serverUrl));

        body.append("<tr>");
        body.append("<td>");
        body.append("<table bgcolor='#f6f5f5' align='center' border='0' cellpadding='0' cellspacing='0' width='91.42857142857143%' style='border-collapse: collapse; color: #6f6d6d; font-family: Arial, sans-serif; font-size: 16px;'>");
        body.append("<tr>");
        body.append("<td colspan='3' align='left'><h1 style='font-weight: normal; font-size: 25px; padding-top: 20px; margin-bottom: 10px;'>Dear " + userEntity.getFirstName() + " " + userEntity.getLastName() + "</h1></td>");
        body.append("</tr>");
        if (status.equals(Status.ACTIVE)) {
            body.append("<tr><td><p>Please login and view your store (" + storeName + ") performance.</p></td></tr>");
            body.append("<tr><td><p><a href='" + serverUrl + "' target='_blank' style='color: #2693ff; text-decoration: none;'>Login to " + Constants.APP_NAME + " Support</a></p></td></tr>");
        } else if (status.equals(Status.INACTIVE)) {
            body.append("<tr><td><p>Please login and view your store (" + storeName + ") performance.</p></td></tr>");
            body.append("<tr><td><p><a href='" + serverUrl + "' target='_blank' style='color: #2693ff; text-decoration: none;'>Login to " + Constants.APP_NAME + " Support</a></p></td></tr>");
        }

        body.append(getEmailFooter(serverUrl));
        return body.toString();
    }
}
