package top.e_learn.learnEnglish.category;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.payload.response.GetCategoryResponse;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.service.FileStorageService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@Data
public class CategoryController {

    @Value("${file.upload-category-image}")
    private String categoryStorePath;

    private final CategoryService categoryService;

    private final FileStorageService fileStorageService;


    @GetMapping("/admin/categories")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewAllCategories.class)
    public ResponseEntity<List<Category>> allCategories(Principal principal) {
        if (principal != null) {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.badRequest().body(new ArrayList<>());
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
        return ResponseEntity.badRequest().body("Access denied");
    }


    @GetMapping("/admin/categories/new-category")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> nwCategory(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.badRequest().body("Access denied");
    }

    @PutMapping("/admin/category/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> savesCategory(@PathVariable String uuid,
                                                               @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                               @RequestPart(value = "category") Category category,
                                                               Principal principal) throws IOException {
        if (principal != null) {
            if (category.getUuid() != null && category.getParentCategory() != null && category.getUuid().equals(category.getParentCategory().getUuid()))
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR));
            try {
                Category categoryDb = categoryService.getCategoryByUuid(uuid);
                category.setImage(new Image());
                if (imageFile != null) {
                    category.getImage().setImageName(fileStorageService.storeFile(imageFile, categoryStorePath, category.getName()));
                    if (categoryDb.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(categoryDb.getImage().getImageName(), categoryStorePath);
                }
                if (category.isMainCategory() && category.getParentCategory() == null) {
                    return ResponseEntity.ok(categoryService.saveMainCategory(category, categoryDb));
                }
                return ResponseEntity.ok(categoryService.saveSubcategory(category, categoryDb));
            } catch (ObjectNotFoundException e) {
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, categoryStorePath, category.getName()));
                category.setImage(image);
                return ResponseEntity.ok(categoryService.saveNewCategory(category));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/subcategories/main-category/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewAllCategories.class)
    public ResponseEntity<List<Category>> getSubcatego(@RequestParam("mainCategoryId") String uuid, Principal principal) {
        if (principal != null && !uuid.isBlank()) {
            return ResponseEntity.ok(categoryService.getSubcategoriesFromMainCategory(uuid));
        }
        return ResponseEntity.notFound().build();
    }
}
