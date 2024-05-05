package top.e_learn.learnEnglish.wordLessonCard;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class WordLessonCardService {

    private final WordLessonCardRepository repository;

    public WordLessonCard getWordInWordLesson(long wordInWordLessonId) {
        Optional<WordLessonCard> wordInWordLessonOptional = repository.findById(wordInWordLessonId);
        if(wordInWordLessonOptional.isPresent()) {
            return wordInWordLessonOptional.get();
        } else throw new ObjectNotFoundException("");
    }

    @Transactional
    public Page<WordLessonCard> wordsFromWordLesson(int page, int size, long wordLessonId) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.wordsFromWordLesson(pageable, wordLessonId);
    }

    @Transactional
    public List<WordLessonCard> wordInWordLessonsToWordLessonAudit(List<Long> wordsId, int wordAuditCounter) {
        List<WordLessonCard> wordLessonCards = repository.findByIds(wordsId);
        for (int i = 0; i < wordLessonCards.size(); i++) {
//            wordInWordLessons.get(i).setTotalPage(wordAuditCounter);
        }
        return wordLessonCards;
    }

    public WordLessonCard getWordForWordLessonAudit(long wordInWordLessonId, int wordAuditCounter, int wordsIdListLength) {
        WordLessonCard wordLessonCard = getWordInWordLesson(wordInWordLessonId);
//        wordInWordLesson.setTotalPage(wordAuditCounter - 1);
        int count = (int) (Math.random() * 10);
        if (count % 2 != 0 && wordsIdListLength > 2) {
            wordLessonCard.setWordAuditSlide("slideAuditRadios");
        }
        return wordLessonCard;
    }
}
