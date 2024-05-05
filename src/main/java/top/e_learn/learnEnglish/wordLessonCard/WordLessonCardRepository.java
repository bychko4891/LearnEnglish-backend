package top.e_learn.learnEnglish.wordLessonCard;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordLessonCardRepository extends CrudRepository<WordLessonCard, Long> {


    @Query("SELECT ww FROM WordLessonCard ww WHERE ww.wordLesson.id = :wordLessonId ORDER BY ww.sortOrder ASC")
    Page<WordLessonCard> wordsFromWordLesson(Pageable pageable, @Param("wordLessonId")Long wordLessonId);

    @Query("SELECT wl FROM WordLessonCard wl WHERE wl.id IN :ids")
    List<WordLessonCard> findByIds(@Param("ids") List<Long> ids);
}
