package top.e_learn.learnEnglish.repository;

import top.e_learn.learnEnglish.model.ApplicationPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageApplicationRepository extends CrudRepository<ApplicationPage, Long> {
}
