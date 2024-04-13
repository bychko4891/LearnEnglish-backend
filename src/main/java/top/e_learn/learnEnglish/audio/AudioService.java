package top.e_learn.learnEnglish.audio;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class AudioService {

    private final AudioRepository audioRepository;


}
