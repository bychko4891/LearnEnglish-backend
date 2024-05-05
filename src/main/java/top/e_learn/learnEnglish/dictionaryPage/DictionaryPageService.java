package top.e_learn.learnEnglish.dictionaryPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.word.Word;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.word.WordService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DictionaryPageService {

    private final DictionaryPageRepository dictionaryPageRepository;

    private final CategoryService categoryService;

    private final WordService wordService;

    public DictionaryPage getDictionaryPageById(long id) {
        Optional<DictionaryPage> vocabularyPageOptional = dictionaryPageRepository.findById(id);
        if (vocabularyPageOptional.isPresent()) {
            return vocabularyPageOptional.get();
        } else throw new RuntimeException("");
    }

    public DictionaryPage getDictionaryPageByUuid(String uuid) {
        return dictionaryPageRepository.findDictionaryPageByUuid(uuid).orElseThrow(() -> new ObjectNotFoundException("Dictionary page with uuid: " + uuid + "not found"));
    }

    public DictionaryPage getDictionaryPageByName(String name) {
        return dictionaryPageRepository.findDictionaryPageByName(name).orElseThrow(() -> new ObjectNotFoundException("Dictionary page with uuid: " + name + "not found"));
    }

    public DictionaryPage getNewDictionaryPage(String uuid) {
        DictionaryPage dictionaryPage = new DictionaryPage();
        dictionaryPage.setUuid(uuid);
        dictionaryPage.setDescription("Add description to new Dictionary page");
        return dictionaryPage;

    }


    public Page<DictionaryPage> getDictionaryPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return dictionaryPageRepository.findAllDictionaryPage(pageable);
    }

    @Transactional
    public void saveDictionaryPage(DictionaryPage dictionaryPageDB, DictionaryPage dictionaryPage) {
        if (!dictionaryPage.getWord().getUuid().equals(dictionaryPageDB.getWord().getUuid())) {
            Word word = wordService.getWordByUuid(dictionaryPage.getWord().getUuid());
            dictionaryPageDB.setWord(word);
            dictionaryPageDB.setName(word.getName());
        }
        Optional.ofNullable(dictionaryPage.getImage().getImageName())
                .ifPresent(imageName -> dictionaryPageDB.getImage().setImageName(imageName));
//        Optional.ofNullable(dictionaryPage.getCardInfo()).ifPresent(dictionaryPageDB::setCardInfo);
        Optional.ofNullable(dictionaryPage.getDescription()).ifPresent(dictionaryPageDB::setDescription);
        Optional.ofNullable(dictionaryPage.getPartOfSpeech()).ifPresent(dictionaryPageDB::setPartOfSpeech);
        Optional.ofNullable(dictionaryPage.getHtmlTagDescription()).ifPresent(dictionaryPageDB::setHtmlTagDescription);
        Optional.ofNullable(dictionaryPage.getHtmlTagTitle()).ifPresent(dictionaryPageDB::setHtmlTagTitle);
        dictionaryPageDB.setPublished(dictionaryPage.isPublished());
        if (dictionaryPage.getCategory() != null) {
            dictionaryPageDB.setCategory(categoryService.getCategoryByUuid(dictionaryPage.getCategory().getUuid()));
        }
        dictionaryPageRepository.save(dictionaryPageDB);
    }

    @Transactional
    public void saveNewDictionaryPage(DictionaryPage dictionaryPage) {
        Word word = wordService.getWordByUuid(dictionaryPage.getWord().getUuid());
        dictionaryPage.setName(word.getName());
        dictionaryPage.setWord(wordService.getWordByUuid(dictionaryPage.getWord().getUuid()));
        if (dictionaryPage.getCategory() != null) {
            dictionaryPage.setCategory(categoryService.getCategoryByUuid(dictionaryPage.getCategory().getUuid()));
        }
        //TODO : Add phrases Application
        dictionaryPageRepository.save(dictionaryPage);
    }

    public boolean existDictionaryPageByName(String vocabularyPageName) {
        String vocabularyPageNameNormalize = StringUtils.normalizeSpace(vocabularyPageName);
        return dictionaryPageRepository.existsDictionaryPageByNameIgnoreCase(vocabularyPageNameNormalize);
    }

    @Transactional
    public List<DictionaryPage> searchDictionaryPageForWordLesson(String searchTerm) {
        return dictionaryPageRepository.findDictionaryPageForWordLesson(searchTerm);
    }

    @Transactional
    public List<DictionaryPage> searchDictionaryPageForUser(String searchTerm) {
        return dictionaryPageRepository.findDictionaryPageUserSearch(searchTerm);
    }

    public CustomResponseMessage verifyUserWord(String wordVerify, long dictionaryPageId) {
        DictionaryPage dictionaryPage = getDictionaryPageById(dictionaryPageId);
        if (dictionaryPage.getName().equals(StringUtils.normalizeSpace(wordVerify))) {
            return new CustomResponseMessage(Message.SUCCESS, dictionaryPage.getName());
        } else return new CustomResponseMessage(Message.ERROR, dictionaryPage.getName());
    }

    public List<DictionaryPage> getDictionaryPagesFromCategory(long categoryId) {
        return dictionaryPageRepository.findAllByCategoryId(categoryId);
    }


}
