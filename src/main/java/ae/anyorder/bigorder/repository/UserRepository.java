package ae.anyorder.bigorder.repository;

import ae.anyorder.bigorder.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Frank on 4/8/2018.
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long>{

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    @Query("select u from #{#entityName} u where id = ?1")
    UserEntity findByUserId(Long id);

    UserEntity findByVerificationCode(String code);
}
