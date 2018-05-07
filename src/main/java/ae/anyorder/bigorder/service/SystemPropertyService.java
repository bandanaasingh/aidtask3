package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.enums.PreferenceType;
import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import ae.anyorder.bigorder.model.PreferencesEntity;

import java.util.List;

/**
 * Created by Frank on 4/9/2018.
 */
public interface SystemPropertyService {

    public void preferenceInitializaton();

    public List<PreferencesEntity> getAllPreferences() throws Exception;

    public PreferenceTypeEntity getAllPreferences(Long typeId) throws Exception;

    public Boolean updateSystemPreferences(List<PreferencesEntity> preferences) throws Exception;

    public Boolean updateSystemPreferencesType(PreferenceTypeEntity preferenceType) throws Exception;

    public String readPrefValue(PreferenceType preferenceType) throws Exception;

}
