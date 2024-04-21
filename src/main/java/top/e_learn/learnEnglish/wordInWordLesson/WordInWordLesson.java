package top.e_learn.learnEnglish.wordInWordLesson;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import top.e_learn.learnEnglish.dictionaryPage.DictionaryPage;
import top.e_learn.learnEnglish.wordLesson.WordLesson;

@Entity
@Table(name = "words_in_word_lesson")
@Data
public class WordInWordLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(columnDefinition = "text")
    @JsonView(JsonViews.ViewFieldUUID.class)
    private String cardInfo;

    @ManyToOne
    @JoinColumn(name = "word_lesson_id")
    @JsonView(JsonViews.ViewFieldOther.class)
    private WordLesson wordLesson;

    @ManyToOne
    @JoinColumn(name = "dictionary_page_id", referencedColumnName = "id")
    @JsonView(JsonViews.ViewFieldWord.class)
    private DictionaryPage dictionaryPage;

    @Column(name = "list_order")
    private Integer listOrder;


    @Transient
    @JsonView(JsonViews.ViewFieldOther.class)
    private String wordAuditSlide;


}
