package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Frank on 4/15/2018.
 */
@Repository
@Transactional
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query("select a from #{#entityName} a where a.id = ?1")
    AddressEntity findByAddressId(Long id);

}
