package top.e_learn.learnEnglish.vocabularyPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.model.Image;
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
import java.util.List;

@RestController
@Data
@RequestMapping("api")
public class VocabularyPageController {

    @Value("${file.upload-vocabulary-page-image}")
    private String wordStorePath;

    private final VocabularyPageService vocabularyPageService;

    private final CategoryService categoryService;

    private final FileStorageService fileStorageService;

    @GetMapping("/vocabulary/pages/category/{uuidCategory}")
    public ResponseEntity<?> getVocabularyPagesFromCategory(@PathVariable String uuidCategory) {
        Category subcategory = categoryService.getCategoryByUuid(uuidCategory);
        return ResponseEntity.ok(vocabularyPageService.getVocabularyPagesFromCategory(subcategory.getId()));
    }

    @PostMapping("/admin/vocabulary-page/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> vocabularyPageSave(@RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                                    @RequestPart(value = "vocabularyPage") VocabularyPage vocabularyPage,
                                                                    Principal principal) throws RuntimeException, IOException {
        if (principal != null) {
            if(vocabularyPage.getWord() == null || vocabularyPage.getWord().getId() == null) {
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_REQUIRED_FIELD));
            }
            try {
                VocabularyPage vocabularyPageDB = vocabularyPageService.getVocabularyPage(vocabularyPage.getId());
                if (!vocabularyPage.getWord().getId().equals(vocabularyPageDB.getWord().getId()) && vocabularyPageService.existVocabularyPageByName(vocabularyPage.getWord().getName())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT));
                }
                vocabularyPage.setImage(new Image());
                if (imageFile != null) {
                    vocabularyPage.getImage().setImageName(fileStorageService.storeFile(imageFile, wordStorePath, vocabularyPage.getWord().getName()));
                    if (vocabularyPageDB.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(vocabularyPageDB.getImage().getImageName(), wordStorePath);
                }
                return ResponseEntity.ok(vocabularyPageService.saveVocabularyPage(vocabularyPageDB, vocabularyPage));

            } catch (RuntimeException e) {
                if (vocabularyPageService.existVocabularyPageByName(vocabularyPage.getWord().getName())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT));
                }
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, wordStorePath, vocabularyPage.getWord().getName()));
                vocabularyPage.setImage(image);
                return ResponseEntity.ok(vocabularyPageService.saveNewVocabularyPage(vocabularyPage));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/search-vocabulary-page-for-lesson")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<List<VocabularyPage>> searchWordForVocabularyPage(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (!searchTerm.isBlank() && principal != null) {
            List<VocabularyPage> vocabularyPageList = vocabularyPageService.searchVocabularyPageForWordLesson(searchTerm);
            return ResponseEntity.ok(vocabularyPageList);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/vocabulary-page/{id}/verify-user-word")
    public ResponseEntity<CustomResponseMessage> wordConfirm(@PathVariable("id") long vocabularyPageId,
                                                             @RequestParam(name = "userWord") String userWord,
                                                             Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(vocabularyPageService.verifyUserWord(userWord, vocabularyPageId));
        }
        return ResponseEntity.notFound().build();
    }

    //    @GetMapping("/admin/vocabulary-pages")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String wordsListAdminPage(@RequestParam(value = "page", defaultValue = "0") int page,
//                                     @RequestParam(value = "size", defaultValue = "30", required = false) int size,
//                                     Principal principal,
//                                     Model model) {
//        if (principal != null) {
//            if (page < 0) page = 0;
//            Page<VocabularyPage> vocabularyPages = vocabularyPageService.getVocabularyPages(page, size);
//            if (vocabularyPages.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", vocabularyPages.getTotalPages());
//            }
//            model.addAttribute("vocabularyPages", vocabularyPages.getContent());
//            model.addAttribute("currentPage", page);
//
//            return "admin/vocabularyPages";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/admin/vocabulary-pages/new-vocabulary-page")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String newVocabularyPage(Principal principal) {
//        if (principal != null) {
//            try {
//                long count = vocabularyPageService.countVocabularyPages() + 1;
//                return "redirect:/admin/vocabulary-pages/vocabulary-page-edit/" + count;
//            } catch (RuntimeException e) {
//                return "redirect:/admin/vocabulary-pages/vocabulary-page-edit/1";
//            }
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/admin/vocabulary-pages/vocabulary-page-edit/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public String editVocabularyPage(@PathVariable("id") long id, Model model, Principal principal) {
//        if (principal != null) {
//            List<Category> mainCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.VOCABULARY_PAGE);
//            model.addAttribute("category", "Відсутня");
//            model.addAttribute("mainCategories", mainCategories);
//            try {
//                VocabularyPage vocabularyPage = vocabularyPageService.getVocabularyPage(id);
//                if (vocabularyPage.getCategory() != null) {
//                    model.addAttribute("category", vocabularyPage.getCategory().getName());
//                }
//                model.addAttribute("vocabularyPage", vocabularyPage);
//            } catch (RuntimeException e) {
//                model.addAttribute("vocabularyPage", vocabularyPageService.getNewVocabularyPage(id));
//            }
//            return "admin/vocabularyPageEdit";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/vocabulary/main-categories")
//    public String wordsMainCategories(Model model) {
//        List<Category> mainCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.VOCABULARY_PAGE);
//        if (mainCategories != null) {
//            model.addAttribute("mainCategories", mainCategories);
//        }
//        return "vocabularyMainCategories";
//    }
//

//
//
//    @GetMapping("/vocabulary/main-category/{uuid}")
//    public String wordsSubcategoriesFromMainCategories(@PathVariable String uuid, Model model) {
//        Category mainCategory = categoryService.getCategoryByUIID(uuid);
//
//        if (mainCategory.isViewSubcategoryFullNoInfoOrNameAndInfo()) {
//            return "vocabularySubcategories_And_Info_Field";
//        } else {
////            List<Category> wordsSubCategoriesAndSubSubInMainCategory = categoryService.getSubcategoriesAndSubSubcategoriesInMainCategory(id);
//            model.addAttribute("mainCategory", mainCategory);
////            model.addAttribute("mainCategoryId", mainCategory.getId());
//            return "vocabularySubcategories_No_Info_Field";
//        }
//    }

}
