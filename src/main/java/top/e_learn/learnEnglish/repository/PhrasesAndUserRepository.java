package top.e_learn.learnEnglish.repository;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.PhraseAndUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhrasesAndUserRepository extends CrudRepository<PhraseAndUser, Long> {

    Optional<PhraseAndUser> findTranslationPairUserByPhraseUser_IdAndUserId(Long translationPairId, Long userId);

}
