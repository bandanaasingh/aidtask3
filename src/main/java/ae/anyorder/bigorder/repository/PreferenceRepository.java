package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.PreferencesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Frank on 4/9/2018.
 */
@Repository
@Transactional
public interface PreferenceRepository extends JpaRepository<PreferencesEntity, Long>{

}
