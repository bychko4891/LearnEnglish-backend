package top.e_learn.learnEnglish.applicationPage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationPageRepository extends CrudRepository<ApplicationPage, Long> {

    Optional<ApplicationPage> findApplicationPageByUuid(String uuid);

    @Query("select a from ApplicationPage a order by a.id asc")
    List<ApplicationPage> getAllAppPages();

    Optional<ApplicationPage> findApplicationPageByUrl(String url);

}
