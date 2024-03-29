package top.e_learn.learnEnglish.repository;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.WordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordUserRepository extends JpaRepository<WordUser, Long>  {

    Optional<WordUser> findWordUserByUserIdAndWordId(Long userId, Long wordId);
}
