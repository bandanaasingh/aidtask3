package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.dto.Page;
import ae.anyorder.bigorder.model.MerchantEntity;
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
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {

    @Query("select a from #{#entityName} a where a.id = ?1")
    MerchantEntity findByMerchantId(Long merchantId);

    MerchantEntity findByBusinessTitle(String businessTitle);

    //Integer getNumberOfMerchant();

    @Query("select a from #{#entityName} a")
    List<MerchantEntity> getMerchantList(Page page);

}
