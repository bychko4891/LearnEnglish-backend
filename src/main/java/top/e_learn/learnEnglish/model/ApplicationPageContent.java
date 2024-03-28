package top.e_learn.learnEnglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import top.e_learn.learnEnglish.utils.JsonViews;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "application_page_contents")
@Data
public class ApplicationPageContent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column(name = "h1")
    @JsonView(JsonViews.ViewFieldOther.class)
    private String h1;

    @Column(name = "description", columnDefinition = "text")
    @JsonView(JsonViews.ViewFieldOther.class)
    private String description;

    @ManyToOne()
    @JoinColumn(name = "page_application_id")
    private ApplicationPage applicationPage;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @PrePersist
    private void init(){
        this.uuid = UUID.randomUUID().toString();
    }
}
