package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.PreferenceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Frank on 4/9/2018.
 */
@Repository
@Transactional
public interface PreferenceTypeRepository extends JpaRepository<PreferenceTypeEntity, Long>{

    @Query("select a from PreferenceTypeEntity a where a.id = ?1")
    PreferenceTypeEntity findByPreferenceTypeId(Long id);
}
