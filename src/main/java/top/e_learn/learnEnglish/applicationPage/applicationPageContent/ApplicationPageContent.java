package top.e_learn.learnEnglish.applicationPage.applicationPageContent;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import top.e_learn.learnEnglish.applicationPage.ApplicationPage;
import top.e_learn.learnEnglish.model.Image;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_page_contents")
@Data
@JsonSerialize(using = CustomAppPageContentSerializer.class)
public class ApplicationPageContent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column
    private int order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "page_application_id")
    private ApplicationPage applicationPage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @ElementCollection(targetClass = PositionContent.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "position_content", joinColumns = @JoinColumn(name = "application_page_content_id"))
    @Enumerated(EnumType.STRING)
    private Set<PositionContent> positionContent = new HashSet<>();

//    @PrePersist
//    private void init(){
//        this.uuid = UUID.randomUUID().toString();
//    }
}
