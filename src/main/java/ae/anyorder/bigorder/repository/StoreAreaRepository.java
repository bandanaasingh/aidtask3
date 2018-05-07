package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.AreaEntity;
import ae.anyorder.bigorder.model.StoreAreaEntity;
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
public interface StoreAreaRepository extends JpaRepository<StoreAreaEntity, Long>{

    @Query("delete from #{#entityName} a where a.store.id=?1 and a.area.id not in ?2")
    void deleteStoresAreaNotInId(Long id, List<Long> areaIds);

}
