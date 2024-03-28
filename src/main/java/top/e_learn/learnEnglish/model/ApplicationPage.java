package top.e_learn.learnEnglish.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.utils.JsonViews;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "application_pages")
public class ApplicationPage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column(name = "name_page")
    @JsonView(JsonViews.ViewFieldOther.class)
    private String namePage;

    @Column
    @JsonView(JsonViews.ViewFieldOther.class)
    private String htmlDescription;

    @Column
    @JsonView(JsonViews.ViewFieldOther.class)
    private String htmlTitle;

    @Column(name = "page_url")
    @JsonView(JsonViews.ViewFieldOther.class)
    private String pageURL;

    @OneToMany(mappedBy = "applicationPage")
    @JsonView(JsonViews.ViewFieldOther.class)
    private List<ApplicationPageContent> contentsAppPage = new ArrayList<>();

    public ApplicationPage() {
    }
    public ApplicationPage(Long id, String namePage, String pageURL) {
        this.id = id;
        this.namePage = namePage;
        this.pageURL = pageURL;
    }

    public ApplicationPage(Long id, String pageURL) {
        this.id = id;
        this.pageURL = pageURL;
    }
    @PrePersist
    private void init(){
        this.uuid = UUID.randomUUID().toString();
    }

}