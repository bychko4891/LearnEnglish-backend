package top.e_learn.learnEnglish.applicationPage;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.applicationPage.applicationPageContent.ApplicationPageContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "application_pages")
@JsonSerialize(using = CustomAppPageSerializer.class)
public class ApplicationPage implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column
    private String h1;


    @Column
    @Size(max = 360, message = "page.bad.size")
    private String htmlTagDescription;

    @Column
    @Size(max = 360, message = "page.bad.size")
    private String htmlTagTitle;

    @Column
    @NotNull(message = "page.bad.url.null")
    @Size(min=1, max = 200, message = "page.bad.url.size")
    @Pattern(regexp = "^/[a-zA-Z1-9-]+((/{1}[a-zA-Z1-9-]+)?)+$|^/{1}$", message = "page.bad.url")
    private String url;

    @OneToMany(mappedBy = "applicationPage")
    private List<ApplicationPageContent> appPageContents = new ArrayList<>();

    public ApplicationPage() {
    }

    public ApplicationPage(Long id, String uuid, String h1, String url) {
        this.id = id;
        this.uuid = uuid;
        this.h1 = h1;
        this.url = url;
    }

    @PrePersist
    private void init(){
        this.uuid = UUID.randomUUID().toString();
    }

}