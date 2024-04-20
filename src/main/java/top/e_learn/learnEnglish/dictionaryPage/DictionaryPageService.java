package top.e_learn.learnEnglish.dictionaryPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

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
    public CustomResponseMessage saveDictionaryPage(DictionaryPage dictionaryPageDB, DictionaryPage dictionaryPage) {
        if(!dictionaryPage.getWord().getId().equals(dictionaryPageDB.getWord().getId())) {
            Word word = wordService.getWordById(dictionaryPage.getWord().getId());
            dictionaryPageDB.setWord(word);
            dictionaryPageDB.setName(word.getName());
        }
        Optional.ofNullable(dictionaryPage.getImage().getImageName())
                .ifPresent(imageName -> dictionaryPageDB.getImage().setImageName(imageName));
//        Optional.ofNullable(dictionaryPage.getCardInfo()).ifPresent(dictionaryPageDB::setCardInfo);
        Optional.ofNullable(dictionaryPage.getDescription()).ifPresent(dictionaryPageDB::setDescription);
        dictionaryPageDB.setPublished(dictionaryPage.isPublished());
        if (dictionaryPage.getCategory() != null && dictionaryPage.getCategory().getId() != 0) {
            if (dictionaryPageDB.getCategory() == null || !dictionaryPage.getCategory().getId().equals(dictionaryPageDB.getCategory().getId())) {
                dictionaryPageDB.setCategory(dictionaryPage.getCategory());
            }
        }
        dictionaryPageRepository.save(dictionaryPageDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }

    @Transactional
    public CustomResponseMessage saveNewDictionaryPage(DictionaryPage dictionaryPage) {
        Word word = wordService.getWordById(dictionaryPage.getWord().getId());
        dictionaryPage.setName(word.getName());
        dictionaryPage.setWord(word);
        if(dictionaryPage.getCategory().getId() == 0) dictionaryPage.setCategory(null);
        if(dictionaryPage.getWord().getId() == null ) dictionaryPage.setWord(null);
        //TODO : Add phrases Application
        dictionaryPageRepository.save(dictionaryPage);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public boolean existDictionaryPageByName(String vocabularyPageName) {
        String vocabularyPageNameNormalize = StringUtils.normalizeSpace(vocabularyPageName);
        return dictionaryPageRepository.existsDictionaryPageByNameIgnoreCase(vocabularyPageNameNormalize);
    }

    @Transactional
    public List<DictionaryPage> searchDictionaryPageForWordLesson(String searchTerm) {
        return dictionaryPageRepository.findDictionaryPageForWordLesson(searchTerm);
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
