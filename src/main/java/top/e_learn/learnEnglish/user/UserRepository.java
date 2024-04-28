package top.e_learn.learnEnglish.user;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUsersByUuid(String uuid);

    Optional<User> findUsersByUniqueServiceCode(String uniqueServiceCode);

    @Query("SELECT u FROM User u WHERE u.id <> 1 ORDER BY u.id ASC")
    Page<User> getPageUsers(Pageable pageable);

//    User findByActivationCode(String code);


}
