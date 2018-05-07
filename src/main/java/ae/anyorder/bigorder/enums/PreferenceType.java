package ae.anyorder.bigorder.enums;

/**
 * Created by Frank on 4/9/2018.
 */
public enum PreferenceType {
    CURRENCY,
    COUNTRY,
    SMS_PROVIDER,     //Sparrow SMS 1, Twilio SMS 2
    SMS_COUNTRY_CODE, //Country code for sms to be sent

    /* Support configuration */
    HELPLINE_NUMBER,      //system helpline number
    ADMIN_EMAIL,        //system administration email
    SUPPORT_EMAIL,      //
    SERVER_URL,

    /* Company Information */
    COMPANY_NAME,     // Name of the company
    COMPANY_LOGO,      //logo of the company
    LOGO_FOR_PDF_EMAIL, //logo to be used in the pdf and email
    COMPANY_ADDRESS,   //address of the company
    CONTACT_NO,        //contact number of the company
    COMPANY_EMAIL,     //email of the company
    COMPANY_WEBSITE,   //website of the company
    REGISTRATION_NO,   //company registration number
    APPLICATION_NAME,   //Name of the application
    DEFAULT_IMG_SEARCH,

    /* Version Configuration */
    ANDROID_APP_VER_NO,
    WEB_APP_VER_NO,
    IOS_APP_VER_NO,

}
