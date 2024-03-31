package top.e_learn.learnEnglish.applicationPage.applicationPageContent;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationPageContentRepository extends CrudRepository<ApplicationPageContent, Long> {

    Optional<ApplicationPageContent> findApplicationPageContentByApplicationPageId(Long applicationPageId);
    Optional<ApplicationPageContent> findApplicationPageContentByUuid(String uuid);

    @Query("select pc from ApplicationPageContent pc order by pc.id asc")
    List<ApplicationPageContent> getAllApplicationPagesContents();
}
