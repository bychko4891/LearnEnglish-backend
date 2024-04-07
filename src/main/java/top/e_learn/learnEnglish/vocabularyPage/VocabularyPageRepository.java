package top.e_learn.learnEnglish.vocabularyPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.vocabularyPage.VocabularyPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyPageRepository extends JpaRepository<VocabularyPage, Long> {

    @Query("SELECT v FROM VocabularyPage v ORDER BY v.id ASC")
    Page<VocabularyPage> findAllVocabularyPage(Pageable pageable);

    @Query("SELECT vp FROM VocabularyPage vp WHERE LOWER(vp.name) LIKE CONCAT(LOWER(:firstLetter), '%') " +
            "AND vp.id NOT IN (SELECT wl.vocabularyPage.id FROM WordInWordLesson wl WHERE wl.vocabularyPage.id IS NOT NULL)")
    List<VocabularyPage> findVocabularyPageForWordLesson(@Param("firstLetter") String firstLetter);

    List<VocabularyPage> findAllByCategoryId(long categoryId);

    boolean existsVocabularyPageByNameIgnoreCase(String vocabularyPageName);
}
