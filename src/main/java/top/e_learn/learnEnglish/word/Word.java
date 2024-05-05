package top.e_learn.learnEnglish.word;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import top.e_learn.learnEnglish.audio.Audio;
import top.e_learn.learnEnglish.utils.JsonViews;

import java.io.Serializable;

@Entity
@Table(name = "words")
@Data
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String uuid;

    @Column
    @NotNull(message = "field.input.notnull")
    @Size(min = 1,max = 45, message = "word.name.size")
    @Pattern(regexp = "^\\S*$", message = "field.input.spaces")
    @Pattern(regexp = "(^[a-zA-Z`']+$)|(^[a-zA-Z]+-[a-zA-Z]+$)", message = "word.name.pattern")
    private String name;

    @Column
    @JsonView(JsonViews.ViewFieldTranslate.class)
    private String translate;

    @Column
    private String brTranscription;

    @Column
    private String usaTranscription;

    @Column
    private String irregularVerbPt;

    @Column
    private String irregularVerbPp;

    @Column
    private boolean activeURL = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_id")
    private Audio audio;


}
