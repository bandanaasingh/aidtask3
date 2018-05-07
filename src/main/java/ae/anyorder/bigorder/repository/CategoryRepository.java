package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.CategoryEntity;
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
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

    @Query("select a from #{#entityName} a where a.id = ?1")
    CategoryEntity findByCategoryId(Long id);

    CategoryEntity findByName(String name);

    @Query("select a from #{#entityName} a")
    List<CategoryEntity> findParentCategories();

    @Query("select a from #{#entityName} a where a.status='ACTIVE'")
    List<CategoryEntity> findActiveParentCategories();

    @Query("select a from #{#entityName} a where a.id in ?1")
    List<CategoryEntity> getCategoryInId(List<Long> ids);
}
