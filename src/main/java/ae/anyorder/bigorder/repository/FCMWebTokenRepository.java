package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.FCMWebTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Frank on 4/10/2018.
 */
@Repository
@Transactional
public interface FCMWebTokenRepository extends JpaRepository<FCMWebTokenEntity, Long> {

    @Query("select a from #{#entityName} a where a.webToken=?1 and a.user.id= ?2")
    FCMWebTokenEntity findByTokenAndUserId(String webToken, Long userId);

    @Query("select a.webToken from #{#entityName} a where a.user.id in ?1")
    List<String> findTokenByUserIds(List<Integer> userIds);
}
