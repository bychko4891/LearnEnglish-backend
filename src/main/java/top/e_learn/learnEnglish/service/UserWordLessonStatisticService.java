package top.e_learn.learnEnglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.dto.DtoUserWordLessonStatistic;
import top.e_learn.learnEnglish.utils.dto.DtoUserWordLessonStatisticToUi;
import top.e_learn.learnEnglish.word.Word;
import top.e_learn.learnEnglish.wordLesson.WordLesson;
import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.model.users.UserWordLessonStatistic;
import top.e_learn.learnEnglish.repository.UserWordLessonStatisticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.word.WordService;
import top.e_learn.learnEnglish.wordLesson.WordLessonService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWordLessonStatisticService {
    private final UserWordLessonStatisticRepository userWordLessonStatisticRepository;

    private final UserWordLessonProgressService userWordLessonProgressService;
    private final WordService wordService;
    private final WordLessonService wordLessonService;

    public List<UserWordLessonStatistic> getUserWordLessonStatisticList(Long userId, Long wordLessonId) {
        return userWordLessonStatisticRepository.findAllByUserIdAndWordLessonId(userId, wordLessonId);
    }

    public void saveUserWordLessonStatistic(DtoUserWordLessonStatistic dtoUserWordLessonStatistics) {
        UserWordLessonStatistic userWordLessonStatistic = new UserWordLessonStatistic();
        Word word = wordService.getWordById(dtoUserWordLessonStatistics.getWordId());
        WordLesson wordLesson = wordLessonService.getWordLesson(dtoUserWordLessonStatistics.getWordLessonId());
        userWordLessonStatistic.setUser(dtoUserWordLessonStatistics.getUser());
        userWordLessonStatistic.setWordLesson(wordLesson);
        userWordLessonStatistic.setWord(word.getName());
//        userWordLessonStatistic.setWordInfo(word.getDescription());
        userWordLessonStatistic.setWordLessonCategoryId(wordLesson.getCategory().getId());
        String wordUserAnswer = StringUtils.normalizeSpace(dtoUserWordLessonStatistics.getUserAnswer());
        userWordLessonStatistic.setUserAnswer(wordUserAnswer);
        if (wordUserAnswer.equalsIgnoreCase(word.getName())) userWordLessonStatistic.setAnswerCorrect(true);
        userWordLessonStatisticRepository.save(userWordLessonStatistic);
    }

    @Transactional
    public void deleteWordLessonStatistic(Long userId, Long wordLessonId) {
        userWordLessonStatisticRepository.removeAllByUserIdAndWordLessonId(userId, wordLessonId);
    }

    public DtoUserWordLessonStatisticToUi resultWordLessonAudit(User user, Long wordLessonId) {
        DtoUserWordLessonStatisticToUi wordLessonStatisticToUi = new DtoUserWordLessonStatisticToUi();
        List<UserWordLessonStatistic> userWordLessonStatisticList = userWordLessonStatisticRepository.findAllByUserIdAndWordLessonId(user.getId(), wordLessonId);
        List<DtoUserWordLessonStatistic> dtoUserWordLessonStatisticList = new ArrayList<>();
        int userAnswerCorrectFalse = 0;
        for (UserWordLessonStatistic arr : userWordLessonStatisticList) {
            if (!arr.isAnswerCorrect()) {
                ++userAnswerCorrectFalse;
                DtoUserWordLessonStatistic dtoUserWordLessonStatistic = new DtoUserWordLessonStatistic();
                dtoUserWordLessonStatistic.setWord(arr.getWord());
                dtoUserWordLessonStatistic.setUserAnswer(arr.getUserAnswer());
                dtoUserWordLessonStatistic.setWordInfo(arr.getWordInfo());
                dtoUserWordLessonStatisticList.add(dtoUserWordLessonStatistic);
            }
        }
        wordLessonStatisticToUi.setDtoUserWordLessonStatisticErrorList(dtoUserWordLessonStatisticList);
        double ratingWordLessonAudit = 100.0;
        if (userAnswerCorrectFalse > 0) {
            ratingWordLessonAudit = Math.round((100 - ((100.0 / userWordLessonStatisticList.size()) * userAnswerCorrectFalse)) * 10.0) / 10.0;
        }
        String message = ratingWordLessonAudit == 100 ? "Гарна робота!" : ratingWordLessonAudit >= 80.0 ? "Рекомендуємо ще разок повторити слова" :
                ratingWordLessonAudit >= 60.0 ? "Рекомендуємо повернутися до вивчення матеріалів." : "Вам потрібно більше практики!";
        wordLessonStatisticToUi.setMessage(message);
        wordLessonStatisticToUi.setTotalWords(userWordLessonStatisticList.size());
        wordLessonStatisticToUi.setRating(ratingWordLessonAudit);
        wordLessonStatisticToUi.setWordLessonCategoryId(userWordLessonStatisticList.get(0).getWordLessonCategoryId());

        userWordLessonProgressService.saveRatingWordLessonAudit(user, wordLessonId, ratingWordLessonAudit);

        return wordLessonStatisticToUi;
    }
}
