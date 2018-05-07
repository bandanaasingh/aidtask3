package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.enums.Status;
import ae.anyorder.bigorder.model.AreaEntity;
import ae.anyorder.bigorder.model.StoreBrandEntity;
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
public interface StoreBrandRepository extends JpaRepository<StoreBrandEntity, Long>{

    @Query("select a from #{#entityName} a where a.id = ?1")
    StoreBrandEntity findByBrandId(Long brandId);

    StoreBrandEntity findByName(String name);

    @Query("select a from #{#entityName} a where a.status = ?1")
    List<StoreBrandEntity> getBrandListByStatus(Status status) throws Exception;

    @Query("select a from #{#entityName} a where a.merchant.id = ?1 and a.status = ?2")
    List<StoreBrandEntity> getBrandListByStatus(Long merchantId, Status status) throws Exception;
}
