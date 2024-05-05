package top.e_learn.learnEnglish.wordLesson;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.payload.response.GetEntityAndMainCategoriesResponse;
import top.e_learn.learnEnglish.payload.response.GetPaginationEntityPage;
import top.e_learn.learnEnglish.utils.CustomFieldError;
import top.e_learn.learnEnglish.utils.JsonViews;
import top.e_learn.learnEnglish.utils.ParserToResponseFromCustomFieldError;
import top.e_learn.learnEnglish.utils.dto.DtoWordToUI;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.wordLessonCard.WordLessonCard;
import top.e_learn.learnEnglish.wordLessonCard.WordLessonCardService;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Data
@RequestMapping("api")
public class WordLessonController {

    private final WordLessonCardService wordLessonCardService;

    private final WordLessonService wordLessonService;

    private final CategoryService categoryService;

    private final MessageSource messageSource;

    private final HttpSession session;


    @GetMapping("/admin/word-lessons")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getWordLessonsForAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "25", required = false) int size,
                                              Principal principal) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<WordLesson> wordLessonPage = wordLessonService.getWordLessonsPage(page, size);
            int totalPages = wordLessonPage.getTotalPages();
            if (wordLessonPage.getTotalPages() == 0) totalPages = 1;
            return ResponseEntity.ok(new GetPaginationEntityPage<>(wordLessonPage.getContent(),
                    totalPages,
                    wordLessonPage.getTotalElements(),
                    wordLessonPage.getNumber()
            ));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/new-word-lesson")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> newWordLessonAdminPage(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/word-lesson/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getDictionaryPageByUuid(@PathVariable String uuid, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.getMainCategories(true);
            try {
                WordLesson wordLesson = wordLessonService.getWordLessonByUuid(uuid);
                return ResponseEntity.ok(new GetEntityAndMainCategoriesResponse<>(wordLesson, mainCategories));
            } catch (ObjectNotFoundException e) {
                WordLesson newWordLesson = wordLessonService.getNewWordLesson(uuid);
                return ResponseEntity.ok(new GetEntityAndMainCategoriesResponse<>(newWordLesson, mainCategories));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @PutMapping("/admin/word-lesson/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> saveWordLesson(@PathVariable String uuid,
                                            @Valid @RequestBody WordLesson wordLesson,
                                            BindingResult bindingResult,
                                            Principal principal) {
        if (principal != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResultMessages(bindingResult));
            }
//            if (!wordService.wordDuplicate(word)) {
//                return ResponseEntity.badRequest().body(new MessageResponse(messageSource.getMessage("word.duplicate", null, currentLocale)));
//            }
//            try {
//                Word wordDB = wordService.getWordByUuid(uuid);
//                word.setAudio(new Audio());
//                if (brAudio != null) {
//                    word.getAudio().setBrAudioName(fileStorageService.storeFile(brAudio, audioStorePath, word.getName()));
//                    if (wordDB.getAudio().getBrAudioName() != null)
//                        fileStorageService.deleteFileFromStorage(wordDB.getAudio().getBrAudioName(), audioStorePath);
//                }
//                if (usaAudio != null) {
//                    word.getAudio().setUsaAudioName(fileStorageService.storeFile(usaAudio, audioStorePath, word.getName()));
//                    if (wordDB.getAudio().getUsaAudioName() != null)
//                        fileStorageService.deleteFileFromStorage(wordDB.getAudio().getUsaAudioName(), audioStorePath);
//                }
//                wordService.saveWord(wordDB, word);
//                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
//
//            } catch (ObjectNotFoundException e) {
//                Audio audio = new Audio();
//                audio.setName(word.getName());
//                if (brAudio != null)
//                    audio.setBrAudioName(fileStorageService.storeFile(brAudio, audioStorePath, word.getName() + "_br_"));
//                if (usaAudio != null)
//                    audio.setUsaAudioName(fileStorageService.storeFile(usaAudio, audioStorePath, word.getName() + "_usa_"));
//                word.setAudio(audio);
//                wordService.saveNewWord(word);
//                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
//            }
        return null;
        }
        return ResponseEntity.status(403).body("Access denied");
    }


//    @PostMapping("/admin/word-lesson/save")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<?> saveWordLesson(@RequestBody WordLesson wordLesson, Principal principal) {
//        if (principal != null) {
//            try {
//                WordLesson wordLessonDB = wordLessonService.getWordLessonById(wordLesson.getId());
//                return ResponseEntity.ok(wordLessonService.saveWordLesson(wordLessonDB, wordLesson));
//            } catch (RuntimeException e) {
//                return ResponseEntity.ok(wordLessonService.saveNewWordLesson(wordLesson));
//            }
//
//        }
//        return ResponseEntity.status(403).body("Access denied");
//    }







































    @GetMapping("/word-lesson/{id}/start-lesson")
    @JsonView(JsonViews.ViewWordForWordLesson.class)
    public ResponseEntity<List<WordLessonCard>> startWordLessonGetFirstTwoWords(@PathVariable("id") long wordLessonId, Principal principal) {
        if (principal != null) {
            Page<WordLessonCard> wordsFromLesson = wordLessonCardService.wordsFromWordLesson(0, 2, wordLessonId);
            List<WordLessonCard> words = wordsFromLesson.getContent();
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-next")
    @JsonView(JsonViews.ViewWordForWordLesson.class)
    public ResponseEntity<WordLessonCard> getNextWordsFromWordLesson(@PathVariable("id") long wordLessonId,
                                                                     @RequestParam(name = "size", defaultValue = "1") int size,
                                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                                     Principal principal) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<WordLessonCard> wordsFromLesson = wordLessonCardService.wordsFromWordLesson(page, size, wordLessonId);
            List<WordLessonCard> words = wordsFromLesson.getContent();
            WordLessonCard word = words.get(0);
//            word.setTotalPage(wordsFromLesson.getTotalPages());
            return ResponseEntity.ok(word);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{wordLessonId}/word-audit-start")
    public ResponseEntity<List<WordLessonCard>> startLessonWordAudit(@PathVariable long wordLessonId, Principal principal) {
        if (principal != null) {
            List<Long> wordsId = wordLessonService.getWordInWordLessonIdsForWordLessonAudit(wordLessonId);
            int wordAuditCounter = (wordsId.size() > 9 && wordsId.size() < 16) ? (int) Math.ceil(wordsId.size() * 0.8) : wordsId.size() > 16 ? (int) Math.ceil(wordsId.size() * 0.6) : wordsId.size();
            Collections.shuffle(wordsId);
            List<Long> wordsIdStart = new ArrayList<>(wordsId.subList(0, 2));
            wordsId.subList(0, 2).clear();
            session.setAttribute("wordsId", wordsId);
            session.setAttribute("totalPage", wordAuditCounter);
            session.setAttribute("wordAuditCounter", wordAuditCounter - 2);
            session.setAttribute("wordLessonId", wordLessonId);
            return ResponseEntity.ok(wordLessonCardService.wordInWordLessonsToWordLessonAudit(wordsIdStart, wordAuditCounter));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/word-lesson/{id}/word-audit-next")
    public ResponseEntity<DtoWordToUI> nextWordForLessonWordAudit(@PathVariable("id") long wordLessonId,
                                                                  Principal principal) {
//        if (principal != null) {
//            List<Long> wordsId = (List<Long>) session.getAttribute("wordsId"); //Додати перевірку довжини масива та на null
//            int totalPage = (int) session.getAttribute("totalPage"); //Додати перевірку довжини масива та на null
//            int wordAuditCounter = (int) session.getAttribute("wordAuditCounter");
//            if (wordsId != null && wordsId.size() != 0 && wordAuditCounter != 0) {
//                --wordAuditCounter;
//                Collections.shuffle(wordsId);
//                Long wordId = wordsId.get(0);
//                wordsId.remove(0);
//                if (wordsId.size() != 0) {
//                    session.setAttribute("wordsId", wordsId);
//                    session.setAttribute("wordAuditCounter", wordAuditCounter);
//                } else {
//                    session.removeAttribute("wordsId");
//                    session.removeAttribute("wordAuditCounter");
//                }
//                return ResponseEntity.ok(wordService.getWordForWordLessonAudit(wordId, totalPage, wordsId.size()));
//            }
//        }
        return ResponseEntity.notFound().build();
    }

    public Map<String, String> bindingResultMessages(BindingResult bindingResult) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        List<CustomFieldError> errorFields = new ArrayList<>();
        try {
            errorFields = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new CustomFieldError(fieldError.getField(), messageSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale)))
                    .collect(Collectors.toList());
            return ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields);
        } catch (NoSuchMessageException e) {
            errorFields.clear();
            errorFields.add(new CustomFieldError("serverError", messageSource.getMessage("server.error", null, currentLocale)));
            return ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields);
        }
    }
}
