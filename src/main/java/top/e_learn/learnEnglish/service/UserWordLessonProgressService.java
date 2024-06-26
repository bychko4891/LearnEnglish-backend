package top.e_learn.learnEnglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.model.users.UserWordLessonProgress;
import top.e_learn.learnEnglish.repository.UserWordLessonProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.wordLesson.WordLessonService;

import java.util.Optional;
// Не Буде змінюватись
@Service
@RequiredArgsConstructor
public class UserWordLessonProgressService {

    private final UserWordLessonProgressRepository userWordLessonProgressRepository;
    private final WordLessonService wordLessonService;

    public void startWordLesson(User user, Long wordLessonId, boolean start) {
        Optional<UserWordLessonProgress> userWordLessonProgressOptional = userWordLessonProgressRepository.findUserWordLessonProgressesByUserIdAndWordLessonId(user.getId(), wordLessonId);
        if (userWordLessonProgressOptional.isPresent()) {
            UserWordLessonProgress userWordLessonProgress = userWordLessonProgressOptional.get();
            userWordLessonProgress.setStartLesson(start);
            userWordLessonProgressRepository.save(userWordLessonProgress);
        } else {
            UserWordLessonProgress userWordLessonProgress = new UserWordLessonProgress();
            userWordLessonProgress.setStartLesson(true);
            userWordLessonProgress.setUser(user);
            userWordLessonProgress.setWordLesson(wordLessonService.getWordLesson(wordLessonId));
            userWordLessonProgressRepository.save(userWordLessonProgress);
        }
    }

    public void saveRatingWordLessonAudit(User user, Long wordLessonId, double wordLessonRating) {
        Optional<UserWordLessonProgress> userWordLessonProgressOptional = userWordLessonProgressRepository.findUserWordLessonProgressesByUserIdAndWordLessonId(user.getId(), wordLessonId);
        if (userWordLessonProgressOptional.isPresent()) {
            UserWordLessonProgress userWordLessonProgress = userWordLessonProgressOptional.get();
            userWordLessonProgress.setRating((int)wordLessonRating);
            userWordLessonProgress.setStartLesson(true);
            userWordLessonProgressRepository.save(userWordLessonProgress);
        } else {
            UserWordLessonProgress userWordLessonProgress = new UserWordLessonProgress();
            userWordLessonProgress.setUser(user);
            userWordLessonProgress.setStartLesson(true);
            userWordLessonProgress.setWordLesson(wordLessonService.getWordLesson(wordLessonId));
            userWordLessonProgress.setRating((int)wordLessonRating);
            userWordLessonProgressRepository.save(userWordLessonProgress);
        }
    }

}
