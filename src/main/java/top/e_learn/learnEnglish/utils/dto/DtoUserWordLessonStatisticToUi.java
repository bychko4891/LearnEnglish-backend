package top.e_learn.learnEnglish.utils.dto;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DtoUserWordLessonStatisticToUi {

    private List<DtoUserWordLessonStatistic> dtoUserWordLessonStatisticErrorList;

    private int totalWords;

    private String message;

    private double rating;

    private Long wordLessonCategoryId;

    public DtoUserWordLessonStatisticToUi() {
    }
}
