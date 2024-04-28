package top.e_learn.learnEnglish.category;

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
import top.e_learn.learnEnglish.article.Article;
import top.e_learn.learnEnglish.article.ArticleService;
import top.e_learn.learnEnglish.payload.response.GetCategoryResponse;
import top.e_learn.learnEnglish.utils.CustomFieldError;
import top.e_learn.learnEnglish.utils.MessageResponse;
import top.e_learn.learnEnglish.utils.ParserToResponseFromCustomFieldError;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.image.Image;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.fileStorage.FileStorageService;
import top.e_learn.learnEnglish.utils.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@Data
public class CategoryController {

    @Value("${file.upload-category-image}")
    private String categoryStorePath;

    private final CategoryService categoryService;

    private final FileStorageService fileStorageService;

    private final ArticleService articleService;

    private final MessageSource messageSource;


    @GetMapping("/admin/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewAllCategories.class)
    public ResponseEntity<?> allCategories(Principal principal) {
        if (principal != null) {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/category/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getCategory(@PathVariable("uuid") String uuid, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.getMainCategories(true);
            try {
                Category category = categoryService.getCategoryByUuid(uuid);
                if (category.isMainCategory()) {
                    mainCategories.removeIf(obj -> obj.getUuid().equals(uuid));
                }
                if(category.getParentCategory() != null) {
                mainCategories.removeIf(obj -> obj.getUuid().equals(category.getParentCategory().getUuid()));
                }
                return ResponseEntity.ok(new GetCategoryResponse(category, mainCategories));
            } catch (ObjectNotFoundException e) {
                Category category = categoryService.getNewCategory(uuid);
                return ResponseEntity.ok(new GetCategoryResponse(category, mainCategories));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }


    @GetMapping("/admin/categories/new-category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> newCategory(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @PutMapping("/admin/category/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> savesCategory(@PathVariable String uuid,
                                                               @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                               @Valid @RequestPart(value = "category") Category category,
                                                               BindingResult bindingResult,
                                                               Principal principal) throws IOException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResultMessages(bindingResult));
        }
        if (principal != null) {
            if (category.getUuid() != null && category.getParentCategory() != null && category.getUuid().equals(category.getParentCategory().getUuid()))
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR));
            try {
                Category categoryDb = categoryService.getCategoryByUuid(uuid);
                category.setImage(new Image());
                if (imageFile != null) {
                    category.getImage().setImageName(fileStorageService.storeFile(imageFile, categoryStorePath, ""));
                    if (categoryDb.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(categoryDb.getImage().getImageName(), categoryStorePath);
                }
                if (category.isMainCategory() && category.getParentCategory() == null) {
                    categoryService.saveMainCategory(categoryDb, category);
                    return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
                }
                categoryService.saveSubcategory(categoryDb, category);
                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
            } catch (ObjectNotFoundException e) {
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, categoryStorePath, ""));
                category.setImage(image);
                categoryService.saveNewCategory(category);
                return ResponseEntity.ok(new MessageResponse(messageSource.getMessage("entity.save.success", null, currentLocale)));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/category/{uuid}")
    public ResponseEntity<?> getCategoryByUuid(@PathVariable String uuid) {
        Category category = categoryService.getCategoryByUuid(uuid);
        List<Article> articles = articleService.findAllArticlesFromCategory(category.getId());
        return ResponseEntity.ok(new GetCategoryResponse(category, articles, new ArrayList<>()));
    }

    @GetMapping("/category/main-categories")
    public ResponseEntity<?> getMainCategoriesByPage(@RequestParam (name = "categoryPage") String categoryPage) {
        return switch (categoryPage) {
            case "dictionary" -> ResponseEntity.ok(categoryService.getMainCategoriesByCategoryPage(true, CategoryPage.DICTIONARY_PAGE));
            case "stories" -> ResponseEntity.ok(categoryService.getMainCategoriesByCategoryPage(true, CategoryPage.MINI_STORIES));
            case "lesson-words" -> ResponseEntity.ok(categoryService.getMainCategoriesByCategoryPage(true, CategoryPage.LESSON_WORDS));
            case "lesson-phrases" -> ResponseEntity.ok(categoryService.getMainCategoriesByCategoryPage(true, CategoryPage.LESSON_PHRASES));
            default -> ResponseEntity.badRequest().body("");
        };
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
