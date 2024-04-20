package top.e_learn.learnEnglish.dictionaryPage;

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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryPageRepository extends JpaRepository<DictionaryPage, Long> {


    Optional<DictionaryPage> findDictionaryPageByUuid(String uuid);

    @Query("SELECT d FROM DictionaryPage d ORDER BY d.id ASC")
    Page<DictionaryPage> findAllDictionaryPage(Pageable pageable);

    @Query("SELECT d FROM DictionaryPage d WHERE LOWER(d.name) LIKE CONCAT(LOWER(:firstLetter), '%') " +
            "AND d.id NOT IN (SELECT wl.dictionaryPage.id FROM WordInWordLesson wl WHERE wl.dictionaryPage.id IS NOT NULL)")
    List<DictionaryPage> findDictionaryPageForWordLesson(@Param("firstLetter") String firstLetter);

    List<DictionaryPage> findAllByCategoryId(long categoryId);

    boolean existsDictionaryPageByNameIgnoreCase(String dictionaryPageName);
}
