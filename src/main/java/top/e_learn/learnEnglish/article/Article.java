package top.e_learn.learnEnglish.article;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.model.Image;

@Entity
@Getter
@Setter
@Table(name="articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column
    @Size(max = 360, message = "page.bad.size")
    private String h1;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    @Size(max = 360, message = "page.bad.size")
    private String htmlTagDescription;

    @Column
    @Size(max = 360, message = "page.bad.size")
    private String htmlTagTitle;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    private Category category;

    public Article() {
    }

    public Article(String uuid, String h1, String description) {
        this.uuid = uuid;
        this.h1 = h1;
        this.description = description;
    }
}
