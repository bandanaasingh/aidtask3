package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Frank on 4/15/2018.
 */
@Repository
@Transactional
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @Query("select a from #{#entityName} a where a.client.id= ?1")
    List<CartEntity> findCartByClientId(Long id) throws Exception;

    @Query("delete from #{#entityName} a where a.client.id= ?1")
    void deleteByCustomerId(Long id) throws Exception;
}
