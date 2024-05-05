package top.e_learn.learnEnglish.wordLessonCard;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.dictionaryPage.DictionaryPage;
import top.e_learn.learnEnglish.utils.JsonViews;
import top.e_learn.learnEnglish.wordLesson.WordLesson;

@Entity
@Getter
@Setter
@Table(name = "word_lesson_cards")
public class WordLessonCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column(columnDefinition = "text")
    @JsonView(JsonViews.ViewFieldUUID.class)
    private String description;

    @ManyToOne
    @JoinColumn(name = "word_lesson_id")
    @JsonView(JsonViews.ViewFieldOther.class)
    private WordLesson wordLesson;

    @ManyToOne
    @JoinColumn(name = "dictionary_page_id", referencedColumnName = "id")
    @JsonView(JsonViews.ViewFieldWord.class)
    private DictionaryPage dictionaryPage;

    @Column(name = "sort_order")
    private int sortOrder;

    @Transient
    @JsonView(JsonViews.ViewFieldOther.class)
    private String wordAuditSlide;

    public WordLessonCard() {
    }
}
