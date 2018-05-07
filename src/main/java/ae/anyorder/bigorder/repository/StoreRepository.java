package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.StoreBrandEntity;
import ae.anyorder.bigorder.model.StoreEntity;
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
public interface StoreRepository extends JpaRepository<StoreEntity, Long>{

    @Query("select a from #{#entityName} a where a.id = ?1")
    StoreEntity findByStoreId(Long id);

    @Query("select a from #{#entityName} a where a.storeBrand.id = ?1")
    List<StoreEntity> findByBrandId(Long brandId);
}
