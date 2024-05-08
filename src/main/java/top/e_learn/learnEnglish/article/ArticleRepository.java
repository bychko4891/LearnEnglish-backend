package top.e_learn.learnEnglish.article;

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

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {

    List<Article> findAllByCategoryId(long categoryId);


    Optional<Article> findArticleByUuid(String uuid);

    @Query("SELECT a FROM Article a ORDER BY a.id ASC")
    Page<Article> findAll(Pageable pageable);


}
