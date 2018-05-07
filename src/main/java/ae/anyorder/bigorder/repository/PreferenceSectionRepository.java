package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.PreferenceSectionEntity;
import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Frank on 4/9/2018.
 */
@Repository
@Transactional
public interface PreferenceSectionRepository extends JpaRepository<PreferenceSectionEntity, Long>{

    @Query("select a from #{#entityName} a where a.group.id = ?1")
    List<PreferenceTypeEntity> findByPreferenceSectionId(Long id);
}
