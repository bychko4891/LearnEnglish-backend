package top.e_learn.learnEnglish.dictionaryPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.model.PhraseApplication;
import top.e_learn.learnEnglish.utils.JsonViews;
import top.e_learn.learnEnglish.word.Word;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dictionary_page")
@Data
public class DictionaryPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.ViewFieldUUID.class)
    private Long id;

    @Column
    private String uuid;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    @Size(max = 360, message = "page.bad.size")
    private String htmlTagDescription;

    @Column
    @Size(max = 360, message = "page.bad.size")
    private String htmlTagTitle;

    @Column
    private boolean published = false;

    @Transient
    private boolean isRepeatable = true;

    @OneToOne
    @JoinColumn(name = "word_id", referencedColumnName = "id")
    @JsonView(JsonViews.ViewFieldWord.class)
    private Word word;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @JsonView(JsonViews.ViewFieldImage.class)
    private Image image;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "phrase_examples",
            joinColumns = @JoinColumn(name = "dictionary_page_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_application_id"))
    private List<PhraseApplication> phraseExamples = new ArrayList<>();


}
