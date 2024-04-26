package top.e_learn.learnEnglish.word;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import top.e_learn.learnEnglish.audio.Audio;
import top.e_learn.learnEnglish.payload.response.GetPaginationEntityPage;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.fileStorage.FileStorageService;
import org.springframework.data.domain.Page;
import top.e_learn.learnEnglish.utils.CustomFieldError;
import top.e_learn.learnEnglish.utils.JsonViews;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import top.e_learn.learnEnglish.utils.MessageResponse;
import top.e_learn.learnEnglish.utils.ParserToResponseFromCustomFieldError;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Data
@RequestMapping("api")
public class WordController {

    @Value("${file.upload-audio}")
    private String audioStorePath;

    private final WordService wordService;

    private final MessageSource messageSource;

    private final FileStorageService fileStorageService;

    @GetMapping("/admin/words")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getWordsForAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "30", required = false) int size,
                                              Principal principal) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<Word> wordPage = wordService.getWordsPage(page, size);
            int totalPages = wordPage.getTotalPages();
            if (wordPage.getTotalPages() == 0) totalPages = 1;
            return ResponseEntity.ok(new GetPaginationEntityPage<>(wordPage.getContent(),
                    totalPages,
                    wordPage.getTotalElements(),
                    wordPage.getNumber()
            ));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/new-word")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> newWordAdminPage(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/word/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getWord(@PathVariable String uuid, Principal principal) {
        if (principal != null) {
            try {
                Word word = wordService.getWordByUuid(uuid);
                return ResponseEntity.ok(word);
            } catch (ObjectNotFoundException e) {
                return ResponseEntity.ok(wordService.getNewWord(uuid));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @PutMapping("/admin/word/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> saveWord(@PathVariable String uuid,
                                      @RequestPart(value = "br", required = false) MultipartFile brAudio,
                                      @RequestPart(value = "usa", required = false) MultipartFile usaAudio,
                                      @Valid @RequestPart(value = "word") Word word,
                                      BindingResult bindingResult,
                                      Principal principal) throws  IOException {
        if (principal != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResultMessages(bindingResult));
            }
            if (!wordService.wordDuplicate(word)) {
                return ResponseEntity.badRequest().body(new MessageResponse(messageSource.getMessage("word.duplicate", null, currentLocale)));
            }
            try {
                Word wordDB = wordService.getWordByUuid(uuid);
                word.setAudio(new Audio());
                if (brAudio != null) {
                    word.getAudio().setBrAudioName(fileStorageService.storeFile(brAudio, audioStorePath, word.getName()));
                    if (wordDB.getAudio().getBrAudioName() != null)
                        fileStorageService.deleteFileFromStorage(wordDB.getAudio().getBrAudioName(), audioStorePath);
                }
                if (usaAudio != null) {
                    word.getAudio().setUsaAudioName(fileStorageService.storeFile(usaAudio, audioStorePath, word.getName()));
                    if (wordDB.getAudio().getUsaAudioName() != null)
                        fileStorageService.deleteFileFromStorage(wordDB.getAudio().getUsaAudioName(), audioStorePath);
                }
                wordService.saveWord(wordDB, word);
                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));

            } catch (ObjectNotFoundException e) {
                Audio audio = new Audio();
                audio.setName(word.getName());
                if (brAudio != null)
                    audio.setBrAudioName(fileStorageService.storeFile(brAudio, audioStorePath, word.getName() + "_br_"));
                if (usaAudio != null)
                    audio.setUsaAudioName(fileStorageService.storeFile(usaAudio, audioStorePath, word.getName() + "_usa_"));
                word.setAudio(audio);
                wordService.saveNewWord(word);
                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/word/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> searchWordForAdmin(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (!searchTerm.isBlank() && principal != null) {
            List<Word> words = wordService.searchWordToAdminPage(searchTerm);
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/search-word-for-vocabulary-page")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<?> searchWordForVocabularyPage(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (!searchTerm.isBlank() && principal != null) {
            List<Word> words = wordService.searchWordForVocabularyPage(searchTerm);
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/search-word/for-phrase")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<?> searchWordForPhrase(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (principal != null && !searchTerm.isBlank()) {
            List<Word> words = wordService.searchWordForPhraseApplication(searchTerm);
            return ResponseEntity.ok(words);
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    private Map<String, String> bindingResultMessages(BindingResult bindingResult) {
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
