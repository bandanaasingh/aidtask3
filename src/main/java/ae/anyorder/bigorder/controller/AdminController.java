package ae.anyorder.bigorder.controller;

import ae.anyorder.bigorder.model.PreferenceSectionEntity;
import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import ae.anyorder.bigorder.model.PreferencesEntity;
import ae.anyorder.bigorder.service.SystemPropertyService;
import ae.anyorder.bigorder.util.GeneralUtil;
import ae.anyorder.bigorder.util.ServiceResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 4/7/2018.
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
    private static final Logger log = Logger.getLogger(AdminController.class);

    @Autowired
    SystemPropertyService systemPropertyService;

    @Autowired
    GeneralUtil generalUtil;

    @RequestMapping(value="/get_group_preferences", method = RequestMethod.GET)
    public Object getGroupPreferences(@RequestHeader String id){
        try{
            //PreferenceTypeEntity returnType = new PreferenceTypeEntity();
            PreferenceTypeEntity preferences = systemPropertyService.getAllPreferences(Long.parseLong(id));
            /*returnType.setId(preferences.getId());
            returnType.setGroupName(preferences.getGroupName());
            List<PreferenceSectionEntity> preferenceSectionEntities = new ArrayList<>();
            for(PreferenceSectionEntity preferenceSection: preferences.getSection()){
                PreferenceSectionEntity preferenceSectionEntity = new PreferenceSectionEntity();
                List<PreferencesEntity> preferencesEntityList = new ArrayList<>();
                for(PreferencesEntity preference: preferenceSection.getPreference()){
                    PreferencesEntity preferencesEntity = new PreferencesEntity();
                    preferencesEntity.setId(preference.getId());
                    preferencesEntity.setPrefKey(preference.getPrefKey());
                    preferencesEntity.setValue(preference.getValue());
                    preferencesEntity.setPrefTitle(preference.getPrefTitle());
                    preferencesEntityList.add(preferencesEntity);
                }
                preferenceSectionEntity.setPreference(preferencesEntityList);
                preferenceSectionEntity.setId(preferenceSection.getId());
                preferenceSectionEntities.add(preferenceSectionEntity);
            }
            returnType.setSection(preferenceSectionEntities);*/
            ServiceResponse serviceResponse = new ServiceResponse("Preferences retrieved successfully");
            serviceResponse.setParams(preferences);
            //return preferences;
            return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
        }catch (Exception e) {
            generalUtil.logError(log, "Error Occurred while getting system preferences", e);
            return new ResponseEntity<>(generalUtil.generateError(e), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
