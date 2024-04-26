package top.e_learn.learnEnglish.word;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.dto.DtoWordToUI;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public Word getWordByUuid(String uuid) {
        return wordRepository.findWordByUuid(uuid).orElseThrow(() -> new ObjectNotFoundException("Article with uuid: " + uuid + "not found"));
    }

    public Word getWordById(long id) {
        return wordRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Article with id: " + id + "not found"));
    }

    public Word getNewWord(String uuid) {
        Word word = new Word();
        word.setUuid(uuid);
        word.setName("name");
        word.setTranslate("translate");
        return word;
    }

    public Page<Word> getWordsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return wordRepository.findAll(pageable);
    }

    @Transactional
    public CustomResponseMessage saveWord(Word wordDB, Word word) {
        String wordName = StringUtils.normalizeSpace(word.getName()).replaceAll("\\s{2,}", " ");
        Optional.of(wordName).ifPresent(wordDB::setName);
        Optional.ofNullable(word.getTranslate()).ifPresent(wordDB::setTranslate);
        Optional.ofNullable(word.getBrTranscription()).ifPresent(wordDB::setBrTranscription);
        Optional.ofNullable(word.getUsaTranscription()).ifPresent(wordDB::setUsaTranscription);
        Optional.ofNullable(word.getIrregularVerbPt()).ifPresent(wordDB::setIrregularVerbPt);
        Optional.ofNullable(word.getIrregularVerbPp()).ifPresent(wordDB::setIrregularVerbPp);
        wordDB.setActiveURL(word.isActiveURL());
        wordRepository.save(saveAudiosNameToWord(wordDB, word));
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }

    private Word saveAudiosNameToWord(Word wordDB, Word word) {
        if (wordDB.getAudio().getUsaAudioName() != null && wordDB.getAudio().getBrAudioName().equals(wordDB.getAudio().getUsaAudioName())
                && word.getAudio().getUsaAudioName() == null && word.getAudio().getBrAudioName() != null
                || word.getAudio().getUsaAudioName() != null && word.getAudio().getBrAudioName() == null) {
            if (word.getAudio().getBrAudioName() != null) {
                wordDB.getAudio().setBrAudioName(word.getAudio().getBrAudioName());
                wordDB.getAudio().setUsaAudioName(word.getAudio().getBrAudioName());
            }
            if (word.getAudio().getUsaAudioName() != null) {
                wordDB.getAudio().setBrAudioName(word.getAudio().getUsaAudioName());
                wordDB.getAudio().setUsaAudioName(word.getAudio().getUsaAudioName());
            }
        }
        Optional.ofNullable(word.getAudio().getBrAudioName()).ifPresent(audioName -> wordDB.getAudio().setBrAudioName(audioName));
        Optional.ofNullable(word.getAudio().getUsaAudioName()).ifPresent(audioName -> wordDB.getAudio().setUsaAudioName(audioName));

        if (wordDB.getAudio().getUsaAudioName() == null && word.getAudio().getBrAudioName() != null)
            wordDB.getAudio().setUsaAudioName(word.getAudio().getBrAudioName());

        if (wordDB.getAudio().getBrAudioName() == null && word.getAudio().getUsaAudioName() != null)
            wordDB.getAudio().setBrAudioName(word.getAudio().getUsaAudioName());
        return wordDB;
    }


    public boolean wordDuplicate(Word word) {
        String wordName = StringUtils.normalizeSpace(word.getName()).replaceAll("\\s{2,}", " ");
        Optional<Word> wordDuplicate = wordRepository.findWordByNameEqualsIgnoreCase(wordName);
        return (wordDuplicate.isEmpty() || wordDuplicate.get().getUuid().equals(word.getUuid()));
    }

    @Transactional
    public CustomResponseMessage saveNewWord(Word word) {
        String wordName = StringUtils.normalizeSpace(word.getName()).replaceAll("\\s{2,}", " ");
        if (word.getAudio().getUsaAudioName() == null)
            word.getAudio().setUsaAudioName(word.getAudio().getBrAudioName());
        if (word.getAudio().getBrAudioName() == null)
            word.getAudio().setBrAudioName(word.getAudio().getUsaAudioName());
        word.setName(wordName);
        wordRepository.save(word);
        return new CustomResponseMessage(Message.SUCCESS_SAVE_WORD_USER);
    }


    public Page<Word> getUserWords(int page, int size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = wordRepository.findAll(pageable, userId);
        List<Word> words = new ArrayList<>();
        for (Object[] result : resultPage.getContent()) {
            Word word = (Word) result[0];
            Boolean isRepeatable = (Boolean) result[1];
//            word.setRepeatable(isRepeatable);
            words.add(word);
        }
        return new PageImpl<>(words, pageable, resultPage.getTotalElements());
    }

    public List<DtoWordToUI> searchWord(String searchTerm) {
//        List<Word> wordsResult = wordRepository.findWord(searchTerm);
//        List<DtoWordToUI> dtoWordToUIList = new ArrayList<>();
//        for (Word arr : wordsResult) {
//            dtoWordToUIList.add(DtoWordToUI.convertToDTO(arr));
//        }
//        return dtoWordToUIList;
        return null;
    }

    @Transactional
    public List<Word> searchWordToAdminPage(String searchTerm) {
        return wordRepository.findWordToDictionaryPageAdminPage(searchTerm);
    }

    @Transactional
    public List<Word> searchWordForVocabularyPage(String searchTerm) {
//        return wordRepository.findWordForVocabularyPage(searchTerm);
        return null;
    }

    @Transactional
    public List<Word> searchWordForPhraseApplication(String searchTerm) {
        return wordRepository.findWordForPhraseApplication(searchTerm);
    }


}
