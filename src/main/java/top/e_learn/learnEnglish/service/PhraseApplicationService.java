package top.e_learn.learnEnglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.model.PhraseApplication;
import top.e_learn.learnEnglish.model.WordWithOrder;
import top.e_learn.learnEnglish.repository.PhraseApplicationRepository;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhraseApplicationService {

    private final PhraseApplicationRepository repository;

    public Long countPhraseApplication() {
        return repository.lastId();
    }

    public PhraseApplication getPhraseApplication(long phraseApplicationId) {
        Optional<PhraseApplication> phraseApplicationOptional = repository.findById(phraseApplicationId);
        if (phraseApplicationOptional.isPresent()) return phraseApplicationOptional.get();
        throw new ObjectNotFoundException("Category with id: " + phraseApplicationId + "not found");
    }

    public PhraseApplication newPhraseApplication(long phraseApplicationId) {
        PhraseApplication phraseApplication = new PhraseApplication();
        phraseApplication.setId(phraseApplicationId);
        phraseApplication.setUkrTranslate("Enter translate");
        return phraseApplication;
    }

    public Page<PhraseApplication> getAllPhraseApplication(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllPhraseApplicationForAdmin(pageable);
    }

    @Transactional
    public CustomResponseMessage saveNewPhraseApplication(PhraseApplication phraseApplication) {
        List<WordWithOrder> wwo = phraseApplication.getEngPhrase();
        for (int i = 0; i < wwo.size(); i++) {
            wwo.get(i).setPhraseApplication(phraseApplication);
        }
        phraseApplication.setEngPhrase(wwo);
        repository.save(phraseApplication);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
    }

    @Transactional
    public CustomResponseMessage savePhraseApplication(PhraseApplication phraseApplicationDB, PhraseApplication phraseApplication) {
        Optional.ofNullable(phraseApplication.getUkrTranslate()).ifPresent(phraseApplicationDB::setUkrTranslate);
        Optional.of(phraseApplication.isQuestionForm()).ifPresent(phraseApplicationDB::setQuestionForm);
        phraseApplicationDB.getEngPhrase().clear();
        List<WordWithOrder> listUI = phraseApplication.getEngPhrase();
        for (int i = 0; i < listUI.size(); i++) {
            listUI.get(i).setPhraseApplication(phraseApplicationDB);
            phraseApplicationDB.getEngPhrase().add(listUI.get(i));
        }
        repository.save(phraseApplicationDB);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
    }
}
