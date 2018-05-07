package ae.anyorder.bigorder.service.impl;

import ae.anyorder.bigorder.dto.Constants;
import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import ae.anyorder.bigorder.model.PreferencesEntity;
import ae.anyorder.bigorder.repository.PreferenceRepository;
import ae.anyorder.bigorder.repository.PreferenceTypeRepository;
import ae.anyorder.bigorder.service.PreferencesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Frank on 4/9/2018.
 */
@Component
public class PreferencesDaoImpl implements PreferencesDao {
    @Autowired
    PreferenceRepository preferenceRepository;

    @Autowired
    PreferenceTypeRepository preferenceTypeRepository;

    @Override
    public List<PreferencesEntity> findAll() throws Exception {
        return preferenceRepository.findAll();
    }

    @Override
    public PreferenceTypeEntity findByPreferenceType(Long id) throws Exception {
        return preferenceTypeRepository.findByPreferenceTypeId(id);
    }

    @Override
    public void updatePreferences(List<PreferencesEntity> preferences) throws Exception {
        //preferenceRepository.save(null);
        saveOrUpdateAll(preferences);
    }

    @Override
    public void updatePreferenceType(PreferenceTypeEntity preferenceType) throws Exception {

    }

    private void saveOrUpdateAll(List<PreferencesEntity> preferences) {
        int i=0;
        for(PreferencesEntity preference: preferences){
            preferenceRepository.save(preference);
            if(++i% Constants.JDBC_BATCH_SIZE ==0)
                preferenceRepository.flush();
        }
    }
}
