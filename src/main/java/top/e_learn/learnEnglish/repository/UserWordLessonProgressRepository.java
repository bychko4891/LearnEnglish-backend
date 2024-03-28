package top.e_learn.learnEnglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.users.UserWordLessonProgress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWordLessonProgressRepository extends CrudRepository<UserWordLessonProgress, Long> {

    Optional<UserWordLessonProgress> findUserWordLessonProgressesByUserIdAndWordLessonId(Long userId, Long wordLessonId);
}
