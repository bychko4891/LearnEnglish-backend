package top.e_learn.learnEnglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.model.PhraseApplication;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.service.PhraseApplicationService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Data
public class PhraseApplicationRestController {

    private final PhraseApplicationService phraseApplicationService;


    @PostMapping("/phrase-application-save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> savePhraseApplication(@RequestBody PhraseApplication phraseApplication,
                                                                       Principal principal) {
        if (principal != null) {
            try {
                PhraseApplication phraseApplicationDB = phraseApplicationService.getPhraseApplication(phraseApplication.getId());
                return ResponseEntity.ok(phraseApplicationService.savePhraseApplication(phraseApplicationDB, phraseApplication));
            } catch (ObjectNotFoundException e) {
                return ResponseEntity.ok(phraseApplicationService.saveNewPhraseApplication(phraseApplication));
            }
//            return ResponseEntity.ok(wordLessonService.saveWordLesson(dtoWordLesson));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

}
