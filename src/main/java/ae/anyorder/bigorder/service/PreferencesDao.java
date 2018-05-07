package ae.anyorder.bigorder.service;

import ae.anyorder.bigorder.model.PreferenceSectionEntity;
import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import ae.anyorder.bigorder.model.PreferencesEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by Frank on 4/9/2018.
 */
public interface PreferencesDao {
    List<PreferencesEntity> findAll() throws Exception;

    PreferenceTypeEntity findByPreferenceType(Long id) throws Exception;

    void updatePreferences(List<PreferencesEntity> systemPreferences) throws Exception;

    void updatePreferenceType(PreferenceTypeEntity preferenceType) throws Exception;
}
