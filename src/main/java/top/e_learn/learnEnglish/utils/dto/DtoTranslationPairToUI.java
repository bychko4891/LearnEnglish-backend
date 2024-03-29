package top.e_learn.learnEnglish.utils.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.users.PhraseUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
public class DtoTranslationPairToUI {
    private Long id;
    private String ukrText;
    private String ukrTextFemale;
    private String engText;
    private String fragment;
    private List<String> engTextList;

    public DtoTranslationPairToUI() {
    }
    public DtoTranslationPairToUI(Long id, String ukrText, String engText) {
    }

    public static DtoTranslationPairToUI convertToDTO(PhraseUser phraseUser) {
        DtoTranslationPairToUI dtoTranslationPairToUI = new DtoTranslationPairToUI();
        dtoTranslationPairToUI.setId(phraseUser.getId());
        dtoTranslationPairToUI.setUkrText(phraseUser.getUkrTranslate());
        dtoTranslationPairToUI.setEngText(phraseUser.getEngPhrase());
        return dtoTranslationPairToUI;
    }
}
