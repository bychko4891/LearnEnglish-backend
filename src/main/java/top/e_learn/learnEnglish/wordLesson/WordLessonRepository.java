package top.e_learn.learnEnglish.wordLesson;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.wordLesson.WordLesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordLessonRepository extends CrudRepository<WordLesson, Long> {

    @Query("SELECT wl FROM WordLesson wl ORDER BY wl.id ASC")
    Page<WordLesson> findAll(Pageable pageable);

    @Query("SELECT MAX(wl.id) FROM WordLesson wl")
    Long lastId();

    int countWordLessonByCategoryId(Long categoryId);

    List<WordLesson> findAllByCategoryIdOrderBySerialNumber(Long categoryId);
}
