package top.e_learn.learnEnglish.model.users;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.wordLesson.WordLesson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.user.User;

@Entity
@Getter
@Setter
@Table(name="user_word_lesson_statistics")
public class UserWordLessonStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "word_lesson_id")
    private WordLesson wordLesson;

    @Column
    private String word;

    @Column
    private String wordInfo;

    @Column
    private String userAnswer;

    @Column
    private Long wordLessonCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean answerCorrect = false;

    public UserWordLessonStatistic() {
    }
}
