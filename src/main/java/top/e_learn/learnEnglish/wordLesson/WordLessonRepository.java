package top.e_learn.learnEnglish.wordLesson;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.e_learn.learnEnglish.article.Article;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordLessonRepository extends CrudRepository<WordLesson, Long> {

    Optional<WordLesson> findWordLessonByUuid(String uuid);

    @Query("SELECT wl FROM WordLesson wl ORDER BY wl.id ASC")
    Page<WordLesson> findAll(Pageable pageable);

    List<WordLesson> findAllByCategoryId(long categoryId);

    int countWordLessonByCategoryId(Long categoryId);

    List<WordLesson> findAllByCategoryIdOrderBySortOrder(Long categoryId);
}
