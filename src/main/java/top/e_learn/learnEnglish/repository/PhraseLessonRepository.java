package top.e_learn.learnEnglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.PhraseLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface PhraseLessonRepository extends CrudRepository<PhraseLesson, Long> {

    @Query("SELECT l FROM PhraseLesson l ORDER BY l.id ASC")
    Page<PhraseLesson> findAll(Pageable pageable);

    @Query("SELECT MAX(pl.id) FROM PhraseLesson pl")
    Long lastId();

}
