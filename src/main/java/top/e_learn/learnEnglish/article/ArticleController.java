package top.e_learn.learnEnglish.article;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.fileStorage.FileStorageService;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.payload.response.GetArticleResponse;
import top.e_learn.learnEnglish.payload.response.GetCategoryResponse;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.utils.CustomFieldError;
import top.e_learn.learnEnglish.utils.MessageResponse;
import top.e_learn.learnEnglish.utils.ParserToResponseFromCustomFieldError;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@Data
public class ArticleController {

    @Value("${file.upload-web-image}")
    private String webImageStorePath;

    private final ArticleService articleService;

    private final CategoryService categoryService;

    private final FileStorageService fileStorageService;

    private final MessageSource messageSource;

    @GetMapping("/admin/articles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getArticles(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(articleService.getAllArticles());
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/articles/new-article")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> newArticle(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/article/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getArticle(@PathVariable("uuid") String uuid, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.getMainCategories(true);
            try {
                Article article = articleService.getArticleByUuid(uuid);
                return ResponseEntity.ok(new GetArticleResponse(article, mainCategories));
            } catch (ObjectNotFoundException e) {
                Article newArticle = articleService.getNewArticle(uuid);
                return ResponseEntity.ok(new GetArticleResponse(newArticle, mainCategories));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/article/{uuid}")
    public ResponseEntity<?> getArticle(@PathVariable("uuid") String uuid) {
        Article article = articleService.getArticleByUuid(uuid);
        return ResponseEntity.ok(article);
    }


    @PutMapping("/admin/article/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> savesCategory(@PathVariable String uuid,
                                           @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                           @Valid @RequestPart(value = "article") Article article,
                                           BindingResult bindingResult,
                                           Principal principal) throws IOException {
        if (principal != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResultMessages(bindingResult));
            }
            try {
                Article articleDb = articleService.getArticleByUuid(uuid);
                article.setImage(new Image());
                if (imageFile != null) {
                    article.getImage().setImageName(fileStorageService.storeFile(imageFile, webImageStorePath, ""));
                    if (articleDb.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(articleDb.getImage().getImageName(), webImageStorePath);
                }
                articleService.saveArticle(articleDb, article);
                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
            } catch (ObjectNotFoundException e) {
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, webImageStorePath, ""));
                article.setImage(image);
                articleService.saveNewArticle(article);
                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
            }
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
