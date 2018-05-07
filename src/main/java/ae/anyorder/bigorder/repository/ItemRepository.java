package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.ComboItemEntity;
import ae.anyorder.bigorder.model.ItemEntity;
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
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query("select a from #{#entityName} a where a.id = ?1")
    ComboItemEntity findByItemId(Long itemId);

    @Query("select a from #{#entityName} a where a.comboItem.id = ?1 and a.id in ?2")
    void deleteItemNotInIds(Long id, List<Long> ids);
}
