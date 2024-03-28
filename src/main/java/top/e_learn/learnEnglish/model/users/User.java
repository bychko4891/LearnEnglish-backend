package top.e_learn.learnEnglish.model.users;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.annotation.JsonView;
import top.e_learn.learnEnglish.model.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.utils.JsonViews;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    @JsonView(JsonViews.ViewFieldUUID.class)
    private String uuid;

    @Column()
    @JsonView(JsonViews.ViewFieldLogin.class)
    private String login;

    @Column(name="name")
    @JsonView(JsonViews.ViewFieldName.class)
    private String name;

    @Column(name = "email")
    @JsonView(JsonViews.ViewFieldEmail.class)
    private String email;

    @Column(name = "unique_service_code", length = 1000)
    private String uniqueServiceCode;

    @Column(name = "active")
    private boolean active;

    @Column(name = "user_phrases_in_lesson")
    private boolean userPhrasesInLesson;

    @Column(name = "user_ip")
    private String userIp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    @JsonView(JsonViews.ViewFieldImage.class)
    private Image userAvatar;

    @Column(name = "password", length = 1000)
    private String password;

    @Column(length = 300)
    @JsonView(JsonViews.ViewFieldUserAbout.class)
    private String about;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private UserStatistics statistics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserWordLessonProgress> wordLessonProgress = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<PhraseUser> phraseUsers = new ArrayList<>();

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @JsonView(JsonViews.ViewFieldAuthority.class)
    private Set<Role> userRole = new HashSet<>();

    @ElementCollection(targetClass = UserGender.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "gender", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @JsonView(JsonViews.ViewFieldGender.class)
    private Set<UserGender> gender = new HashSet<>();

    @Column(name = "date_of_created")
    @JsonView(JsonViews.ViewFieldDateCreate.class)
    private LocalDateTime dateOfCreated;

    @Column(name = "last_visit")
    @JsonView(JsonViews.ViewFieldLastVisit.class)
    private LocalDateTime lastVisit;
    @PrePersist
    private void init(){
        this.dateOfCreated = LocalDateTime.now();
        this.uuid = UUID.randomUUID().toString();
    }

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

}
