package top.e_learn.learnEnglish.controllers.restConrollers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.PhraseLesson;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.service.PhraseLessonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class PhraseLessonRestController {


    private final PhraseLessonService service;

    @PostMapping("/admin/phrase-lessons/phrase-lesson-save/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> phraseLessonSave(@PathVariable(name = "id") long id,
                                                                  @RequestBody PhraseLesson phraseLesson,
                                                                  Principal principal) {
        if (principal != null) {
//            if(phraseLesson.getId() == null) throw new RuntimeException("Method 'phraseLessonSave' id - NULL");
//            try {
//
//            } catch (RuntimeException e) {
//
//            }
//
//
////            return ResponseEntity.ok(phraseLessonService.saveLesson(phraseLesson)); // Додати перевірку на новий чи вже існуючий !!!!!
        return null;
        }
        return ResponseEntity.notFound().build();
    }

}
