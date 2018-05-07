package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.enums.PreferenceType;
import ae.anyorder.bigorder.model.PreferenceSectionEntity;
import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import ae.anyorder.bigorder.model.PreferencesEntity;
import ae.anyorder.bigorder.repository.PreferenceSectionRepository;
import ae.anyorder.bigorder.repository.UserRepository;
import ae.anyorder.bigorder.service.PreferencesDao;
import ae.anyorder.bigorder.service.SystemPropertyService;
import ae.anyorder.bigorder.util.MessageBundle;
import ae.anyorder.bigorder.util.ReturnJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Created by Frank on 4/9/2018.
 */
@Service
public class SystemPropertyServiceImpl implements SystemPropertyService {
    private static final Logger log = Logger.getLogger(SystemPropertyServiceImpl.class);
    private static String SYSTEM_PREF_FILE = "system_pref.properties";

    @Autowired
    PreferencesDao preferencesDao;

    @Autowired
    UserRepository userRepository;

    /*@Autowired
    ActionLogDao actionLogDaoService;*/

    @Autowired
    PreferenceSectionRepository preferenceSectionRepository;

    @Autowired
    ReturnJsonUtil returnJsonUtil;

    @Override
    public void preferenceInitializaton() {
        OutputStream output = null;
        try {
            log.info("Initializing the system_pref property file: " + new ClassPathResource(SYSTEM_PREF_FILE).getURL().toString());
            List<PreferencesEntity> preferences = preferencesDao.findAll();
            if(preferences.size()>0)
                savePropertyFileToWEB_INF(preferences);
        } catch (Exception e) {
            log.error("Error occurred while initializing system preferences", e);
        } finally {

            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    log.error("Error occurred while initializing system preferences", e);
                }
            }
        }
    }

    @Override
    public List<PreferencesEntity> getAllPreferences() throws Exception {
        log.info("---------------------Fetching All Preferences--------------------------");
        return preferencesDao.findAll();
    }

    @Override
    public PreferenceTypeEntity getAllPreferences(Long id) throws Exception {
        log.info("---------------------Fetching All Preferences--------------------------");
        PreferenceTypeEntity preferenceTypeEntity = preferencesDao.findByPreferenceType(id);


        String fields = "id,groupName";

        Map<String, String> assoc = new HashMap<>();
        Map<String, String> subAssoc = new HashMap<>();

        assoc.put("section", "id,section,preference");
        subAssoc.put("preference", "id,prefKey,value,prefTitle");

        return ((PreferenceTypeEntity) returnJsonUtil.getJsonObject(preferenceTypeEntity, fields, assoc, subAssoc));
    }

    private void savePropertyFileToWEB_INF(List<PreferencesEntity> allPreferences) throws Exception {
        log.info("Saving PreferencesEntity in " + SYSTEM_PREF_FILE);
        Resource resource = new ClassPathResource(SYSTEM_PREF_FILE);
        File file = resource.getFile();

        FileInputStream in = new FileInputStream(file);
        Properties prop = new Properties();
        prop.load(in);
        in.close();

        FileOutputStream outputStream = new FileOutputStream(file);

        //Properties prop = new Properties();
        for (PreferencesEntity preferencesEntity : allPreferences) {
            prop.setProperty(preferencesEntity.getPrefKey().toString(), preferencesEntity.getValue());
        }
        prop.store(outputStream, null);
        outputStream.close();
    }

    @Override
    public Boolean updateSystemPreferences(List<PreferencesEntity> systemPreferences) throws Exception {
        log.info("+++++++++++ Updating preferences table and property file ++++++++++++++");
        //update preference in db
        /*List<ActionLogEntity> actionLogs = new ArrayList<>();
        UserEntity userEntity = userDao.find(SessionManager.getUserIds());
        for (PreferencesEntity preference : systemPreferences) {
            PreferencesEntity dbPreference = preferencesDao.findByKey(preference.getPrefKey());
            ActionLogEntity actionLog = ActionLogUtil.createActionLog(dbPreference, preference, new String[]{"value"}, userEntity);
            if (actionLog != null) {
                actionLogs.add(actionLog);
            }
        }*/

        preferencesDao.updatePreferences(systemPreferences);
        /*for (PreferencesEntity preference : systemPreferences) {
            if (preference.getPrefKey().equals(PreferenceTypeEntity.ANDROID_APP_VER_NO.toString())) {
                String androidVersionNo = readPrefValue(PreferenceTypeEntity.ANDROID_APP_VER_NO);
                String message = MessageBundle.getPushNotificationMsg("MOBVER001");
                if (!preference.getValue().equals(androidVersionNo)) {
                    sendNotification(UserRole.ROLE_CUSTOMER, "ANDROID", NotifyTo.CUSTOMER_ANDROID, message, preference.getValue());
                }
            } else if (preference.getPrefKey().equals(PreferenceTypeEntity.IOS_APP_VER_NO.toString())) {
                String iosVersionNo = readPrefValue(PreferenceTypeEntity.IOS_APP_VER_NO);
                String message = MessageBundle.getPushNotificationMsg("MOBVER001");
                if (!preference.getValue().equals(iosVersionNo)) {
                    sendNotification(UserRole.ROLE_CUSTOMER, "MAC_OS_X", NotifyTo.CUSTOMER_IOS, message, preference.getValue());
                }
            }
        }*/
        //update preference in property fle
        savePropertyFileToWEB_INF(systemPreferences);
        return true;
    }


    @Override
    public Boolean updateSystemPreferencesType(PreferenceTypeEntity preferenceTypeEntity) throws Exception {
        log.info("+++++++++++ Updating preferences table and property file ++++++++++++++");
        //update preference in db
        preferencesDao.updatePreferenceType(preferenceTypeEntity);
        List<PreferencesEntity> systemPreferences = new ArrayList<>();
        for (PreferenceSectionEntity sectionEntity : preferenceTypeEntity.getSection()) {
            systemPreferences.addAll(sectionEntity.getPreference());
        }

        //update preference in property fle
        savePropertyFileToWEB_INF(systemPreferences);
        return true;
    }

    @Override
    public String readPrefValue(PreferenceType preferenceTypeEntity) throws Exception {
        Resource resource = new ClassPathResource(SYSTEM_PREF_FILE);
        return MessageBundle.getPropertyKey(preferenceTypeEntity.toString(), resource.getFile());
    }
}