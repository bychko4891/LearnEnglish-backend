package top.e_learn.learnEnglish.wordLesson;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.article.Article;
import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.dictionaryPage.DictionaryPage;
import top.e_learn.learnEnglish.dictionaryPage.DictionaryPageService;
import top.e_learn.learnEnglish.model.users.UserWordLessonProgress;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.word.WordService;
import top.e_learn.learnEnglish.wordLessonCard.WordLessonCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordLessonService {

    private final WordLessonRepository wordLessonRepository;

    private final CategoryService categoryService;

    private final DictionaryPageService dictionaryPageService;


    private final WordService wordService;

    public Page<WordLesson> getWordLessonsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordLessonRepository.findAll(pageable);
    }

    public WordLesson getWordLessonByUuid(String uuid) {
        return wordLessonRepository.findWordLessonByUuid(uuid).orElseThrow(() -> new ObjectNotFoundException("Article with uuid: " + uuid + "not found"));
    }

    public int countWordLessonInCategory(long categoryId) {
        return wordLessonRepository.countWordLessonByCategoryId(categoryId);
    }

    public WordLesson getWordLessonById(Long id) {
        Optional<WordLesson> wordLessonOptional = wordLessonRepository.findById(id);
        if (wordLessonOptional.isPresent()) {
            return wordLessonOptional.get();
        } else throw new RuntimeException("WordLesson no exist");
    }

    public WordLesson getNewWordLesson(String uuid) {
        WordLesson wordLesson = new WordLesson();
        wordLesson.setUuid(uuid);
        wordLesson.setSortOrder(0);
        wordLesson.setName("Enter new name");
        wordLesson.setDescription("Enter description");


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
        Optional.of(wordLesson.getSortOrder()).ifPresent(wordLessonDB::setSortOrder);
        wordLessonDB.getCards().clear();
        List<WordLessonCard> requestCards = wordLesson.getCards();
        for (int i = 0; i < requestCards.size(); i++) {
            requestCards.get(i).setWordLesson(wordLessonDB);
            requestCards.get(i).setDictionaryPage(dictionaryPageService.getDictionaryPageByUuid(requestCards.get(i).getDictionaryPage().getUuid()));
            wordLessonDB.getCards().add(requestCards.get(i));
        }
        if (wordLesson.getCategory() != null && !wordLesson.getCategory().getUuid().isBlank()) {
            wordLessonDB.setCategory(categoryService.getCategoryByUuid(wordLesson.getCategory().getUuid()));
        }
        wordLessonRepository.save(wordLessonDB);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    @Transactional
    public CustomResponseMessage saveNewWordLesson(WordLesson wordLesson) {
        if (wordLesson.getCategory() != null && !wordLesson.getCategory().getUuid().isBlank()) {
            wordLesson.setCategory(categoryService.getCategoryByUuid(wordLesson.getCategory().getUuid()));
        }
        List<WordLessonCard> wordLessonCards = wordLesson.getCards();
        for (int i = 0; i < wordLessonCards.size(); i++) {
            wordLessonCards.get(i).setDictionaryPage(dictionaryPageService.getDictionaryPageByUuid(wordLessonCards.get(i).getDictionaryPage().getUuid()));
            wordLessonCards.get(i).setWordLesson(wordLesson);
        }
        wordLesson.setCards(wordLessonCards);
        wordLessonRepository.save(wordLesson);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public List<WordLesson> findAllWordLessonsFromCategory(long categoryId) {
        return wordLessonRepository.findAllByCategoryId(categoryId);
    }

    public List<WordLesson> getWordLessonsCategory(User user, Long categoryId) {
        List<WordLesson> wordLessonList = wordLessonRepository.findAllByCategoryIdOrderBySortOrder(categoryId);
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
        WordLesson wordLesson = wordLessonRepository.findById(wordLessonId).get();
        List<Long> wordsId = new ArrayList<>();
        for (WordLessonCard arr : wordLesson.getCards()) {
            wordsId.add(arr.getId());
        }
        return wordsId;
    }


}
