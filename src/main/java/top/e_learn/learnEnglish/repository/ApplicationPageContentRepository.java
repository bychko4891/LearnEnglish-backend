package top.e_learn.learnEnglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.ApplicationPageContent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationPageContentRepository extends CrudRepository<ApplicationPageContent, Long> {

    Optional<ApplicationPageContent> findApplicationPageContentByApplicationPageId(Long applicationPageId);
}
