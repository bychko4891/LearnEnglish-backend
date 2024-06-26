package top.e_learn.learnEnglish.wordLesson;

import top.e_learn.learnEnglish.wordInWordLesson.WordInWordLesson;
import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.model.users.UserWordLessonProgress;
import top.e_learn.learnEnglish.category.CategoryRepository;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.word.WordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class WordLessonService {

    private final WordLessonRepository repository;
    private final CategoryRepository categoryRepository;
    private final WordService wordService;

    public Page<WordLesson> getWordLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public long countWordLesson() {
        return repository.lastId();
    }

    public int countWordLessonInCategory(long categoryId) {
        return repository.countWordLessonByCategoryId(categoryId);
    }

    public WordLesson getWordLesson(Long id) {
        Optional<WordLesson> wordLessonOptional = repository.findById(id);
        if (wordLessonOptional.isPresent()) {
            return wordLessonOptional.get();
        } else throw new RuntimeException("WordLesson no exist");
    }

    public WordLesson getNewWordLesson(Long id) {
        WordLesson wordLesson = new WordLesson();
        wordLesson.setName("Enter new name");
        wordLesson.setDescription("Enter description");
        wordLesson.setId(id);
        /////////// Переробити !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        if (id != null && id.size() > 0) {
//            for (PhraseApplication arr : id) {
//                PhraseApplication pa = new PhraseApplication();
//                pa.setId(arr);
//                word.getPhraseExamples().add(pa);
//            }
//        }
        /////////////
        return wordLesson;
    }


    @Transactional
    public CustomResponseMessage saveWordLesson(WordLesson wordLessonDB, WordLesson wordLesson) {
        Optional.ofNullable(wordLesson.getName()).ifPresent(wordLessonDB::setName);
        Optional.ofNullable(wordLesson.getDescription()).ifPresent(wordLessonDB::setDescription);
        Optional.of(wordLesson.getSerialNumber()).ifPresent(wordLessonDB::setSerialNumber);
        wordLessonDB.getWords().clear();
        List<WordInWordLesson> listUI = wordLesson.getWords();
        for (int i = 0; i < listUI.size(); i++) {
            listUI.get(i).setWordLesson(wordLessonDB);
            wordLessonDB.getWords().add(listUI.get(i));
        }
        if (wordLessonDB.getCategory() == null && wordLesson.getCategory().getId() != 0 || wordLesson.getCategory().getId() != 0 && !wordLessonDB.getCategory().getId().equals(wordLesson.getCategory().getId())) {
            wordLessonDB.setCategory(wordLesson.getCategory());
        }
        repository.save(wordLessonDB);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    @Transactional
    public CustomResponseMessage saveNewWordLesson(WordLesson wordLesson) {
        if (wordLesson.getCategory().getId() == 0) wordLesson.setCategory(null);
        List<WordInWordLesson> words = wordLesson.getWords();
        for (int i = 0; i < words.size(); i++) {
            words.get(i).setWordLesson(wordLesson);
        }
        wordLesson.setWords(words);
        repository.save(wordLesson);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public List<WordLesson> getWordLessonsCategory(User user, Long categoryId) {
        List<WordLesson> wordLessonList = repository.findAllByCategoryIdOrderBySerialNumber(categoryId);
        List<UserWordLessonProgress> userWordLessonProgressList = user.getWordLessonProgress();
        if (userWordLessonProgressList.size() != 0) {
            for (WordLesson wordLesson : wordLessonList) {
                for (UserWordLessonProgress arrP : userWordLessonProgressList) {
                    if (wordLesson.getId().equals(arrP.getWordLesson().getId())) {
                        wordLesson.setUserWordLessonProgress(arrP);
                    }
                }
            }
        }
        return wordLessonList;
    }

    public List<Long> getWordInWordLessonIdsForWordLessonAudit(long wordLessonId) {
        WordLesson wordLesson = repository.findById(wordLessonId).get();
        List<Long> wordsId = new ArrayList<>();
        for (WordInWordLesson arr : wordLesson.getWords()) {
            wordsId.add(arr.getId());
        }
        return wordsId;
    }


}
