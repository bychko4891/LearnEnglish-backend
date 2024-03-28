package top.e_learn.learnEnglish.utils.dto;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.Category;
import top.e_learn.learnEnglish.model.MiniStory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class DtoTranslationPairsPage {

    private MiniStory miniStory;
    private Category mainCategorySelect;
    private Category subcategorySelect;
    private Category subSubcategorySelect;
    private List<Long> translationPairsId;

    public DtoTranslationPairsPage() {
    }
}
