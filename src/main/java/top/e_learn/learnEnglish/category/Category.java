package top.e_learn.learnEnglish.category;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.model.CategoryPage;
import top.e_learn.learnEnglish.model.Image;

import java.io.Serializable;
import java.util.*;



@Entity
@Setter
@Getter
@Table(name = "categories")
@JsonSerialize(using = CustomCategorySerializer.class)
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column
    private boolean mainCategory = false;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @Column(name="show_description_in_page")
    private boolean showDescriptionInPage = true;

    @ElementCollection(targetClass = CategoryPage.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "category_pages", joinColumns = @JoinColumn(name = "category_id"))
    @Enumerated(EnumType.STRING)
    private Set<CategoryPage> categoryPage = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Category> subcategories = new ArrayList<>();

    @Transient
    private int countWordLessons;

    @PrePersist
    private void init(){
        this.uuid = UUID.randomUUID().toString();
    }

    public Category() {
    }



}
