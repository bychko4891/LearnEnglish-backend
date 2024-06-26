package top.e_learn.learnEnglish.model;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.word.Word;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name="word_user")
public class WordUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "is_repetable")
    private boolean isRepeatable = true;

    public WordUser() {
    }
}
