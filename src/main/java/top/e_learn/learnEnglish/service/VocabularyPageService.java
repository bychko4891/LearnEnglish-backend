package top.e_learn.learnEnglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.VocabularyPage;
import top.e_learn.learnEnglish.model.Word;
import top.e_learn.learnEnglish.repository.VocabularyPageRepository;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VocabularyPageService {

    private final VocabularyPageRepository vocabularyPageRepository;

    private final WordService wordService;

    public VocabularyPage getVocabularyPage(long id) {
        Optional<VocabularyPage> vocabularyPageOptional = vocabularyPageRepository.findById(id);
        if (vocabularyPageOptional.isPresent()) {
            return vocabularyPageOptional.get();
        } else throw new RuntimeException("");
    }

    public VocabularyPage getNewVocabularyPage(long id) {
        VocabularyPage vocabularyPage= new VocabularyPage();
        vocabularyPage.setId(id);
        vocabularyPage.setDescription("Enter text");
        return vocabularyPage;
    }


    public Page<VocabularyPage> getVocabularyPages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vocabularyPageRepository.findAllVocabularyPage(pageable);
    }

    public long countVocabularyPages() {
        return vocabularyPageRepository.lastId();
    }

    @Transactional
    public CustomResponseMessage saveVocabularyPage(VocabularyPage vocabularyPageDB, VocabularyPage vocabularyPage) {
        if(!vocabularyPage.getWord().getId().equals(vocabularyPageDB.getWord().getId())) {
            Word word = wordService.getWord(vocabularyPage.getWord().getId());
            vocabularyPageDB.setWord(word);
            vocabularyPageDB.setName(word.getName());
        }
        Optional.ofNullable(vocabularyPage.getImage().getImageName())
                .ifPresent(imageName -> vocabularyPageDB.getImage().setImageName(imageName));
        Optional.ofNullable(vocabularyPage.getCardInfo()).ifPresent(vocabularyPageDB::setCardInfo);
        Optional.ofNullable(vocabularyPage.getDescription()).ifPresent(vocabularyPageDB::setDescription);
        vocabularyPageDB.setPublished(vocabularyPage.isPublished());
        if (vocabularyPage.getCategory() != null && vocabularyPage.getCategory().getId() != 0) {
            if (vocabularyPageDB.getCategory() == null || !vocabularyPage.getCategory().getId().equals(vocabularyPageDB.getCategory().getId())) {
                vocabularyPageDB.setCategory(vocabularyPage.getCategory());
            }
        }
        vocabularyPageRepository.save(vocabularyPageDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }

    @Transactional
    public CustomResponseMessage saveNewVocabularyPage(VocabularyPage vocabularyPage) {
        Word word = wordService.getWord(vocabularyPage.getWord().getId());
        vocabularyPage.setName(word.getName());
        vocabularyPage.setWord(word);
        if(vocabularyPage.getCategory().getId() == 0) vocabularyPage.setCategory(null);
        if(vocabularyPage.getWord().getId() == null ) vocabularyPage.setWord(null);
        //TODO : Add phrases Application
        vocabularyPageRepository.save(vocabularyPage);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public boolean existVocabularyPageByName(String vocabularyPageName) {
        String vocabularyPageNameNormalize = StringUtils.normalizeSpace(vocabularyPageName);
        return vocabularyPageRepository.existsVocabularyPageByNameIgnoreCase(vocabularyPageNameNormalize);
    }

    @Transactional
    public List<VocabularyPage> searchVocabularyPageForWordLesson(String searchTerm) {
        return vocabularyPageRepository.findVocabularyPageForWordLesson(searchTerm);
    }

    public CustomResponseMessage verifyUserWord(String wordVerify, long vocabularyPageId) {
        VocabularyPage vocabularyPage = getVocabularyPage(vocabularyPageId);
            if (vocabularyPage.getName().equals(StringUtils.normalizeSpace(wordVerify))) {
                return new CustomResponseMessage(Message.SUCCESS, vocabularyPage.getName());
            } else return new CustomResponseMessage(Message.ERROR, vocabularyPage.getName());
    }



}
