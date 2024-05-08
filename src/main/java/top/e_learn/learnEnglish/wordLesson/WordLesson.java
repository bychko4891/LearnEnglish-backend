package top.e_learn.learnEnglish.wordLesson;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Size;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.wordLessonCard.WordLessonCard;
import top.e_learn.learnEnglish.model.users.UserWordLessonProgress;
import top.e_learn.learnEnglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "word_lessons")
@JsonSerialize(using = CustomWordLessonSerializer.class)
public class WordLesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonView(JsonViews.ViewFieldUUID.class)
    private Long id;

    @Column
    private String uuid;

    @Column
    @Size(max = 30, message = "word.lesson.name.size")
    private String name;

    @Column
    @Size(max = 80, message = "word.lesson.description.size")
    private String description;

    @Column
    private int sortOrder;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "wordLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder")
    private List<WordLessonCard> cards = new ArrayList<>();

    @Transient
    private UserWordLessonProgress userWordLessonProgress;

    public WordLesson() {
    }
}
