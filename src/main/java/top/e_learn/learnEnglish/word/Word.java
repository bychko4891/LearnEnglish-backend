package top.e_learn.learnEnglish.word;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.audio.Audio;
import top.e_learn.learnEnglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "words")
@Data
public class Word implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonView(JsonViews.ViewWordId.class)
    private Long id;

    @Column
    private String uuid;

    @Column
    @JsonView(JsonViews.ViewFieldName.class)
    private String name;

    @Column
    private String translate;

    @Column
    private String brTranscription;

    @Column
    @JsonView(JsonViews.ViewFieldOther.class)
    private String usaTranscription;

    @Column
    private String irregularVerbPt;

    @Column
    private String irregularVerbPp;

    @Column
    private boolean activeURL = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_id")
    @JsonView(JsonViews.ViewFieldAudio.class)
    private Audio audio;


}
