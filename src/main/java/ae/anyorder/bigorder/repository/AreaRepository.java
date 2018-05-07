package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.model.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Frank on 4/8/2018.
 */
@Repository
@Transactional
public interface AreaRepository extends JpaRepository<AreaEntity, Long>{

    @Query("select a from #{#entityName} a where a.id = ?1")
    AreaEntity findByAreaId(Long id);

    @Query("select a from #{#entityName} a where a.parent.id= ?1")
    List<AreaEntity> findChildOfParentArea(Long parentId);

    @Query("select a from #{#entityName} a where a.parent = null")
    List<AreaEntity> findParentAreas();

    @Query("select a from #{#entityName} a where a.status= ?1 and a.parent = null")
    List<AreaEntity> findActiveParentAreas(Status status);
}
