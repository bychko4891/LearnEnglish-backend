package top.e_learn.learnEnglish.model;

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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vocabulary_page")
@Data
public class VocabularyPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.ViewFieldUUID.class)
    private Long id;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String name;

    @Column(columnDefinition = "text")
    @JsonView(JsonViews.ViewFieldUUID.class)
    private String cardInfo;

    @Column(columnDefinition = "text")
    private String description;

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
            joinColumns = @JoinColumn(name = "vocabulary_page_id"),
            inverseJoinColumns = @JoinColumn(name = "phrase_application_id"))
    private List<PhraseApplication> phraseExamples = new ArrayList<>();


}
