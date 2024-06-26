package top.e_learn.learnEnglish.dictionaryPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.payload.response.GetEntityAndMainCategoriesResponse;
import top.e_learn.learnEnglish.payload.response.GetPaginationEntityPage;
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
import top.e_learn.learnEnglish.utils.ValidateFields;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
@Data
@RequestMapping("api")
public class DictionaryPageController {

    @Value("${file.upload-vocabulary-page-image}")
    private String wordStorePath;

    private final DictionaryPageService dictionaryPageService;

    private final CategoryService categoryService;

    private final FileStorageService fileStorageService;

    private final ValidateFields validateFields;

    private final MessageSource messageSource;

    @GetMapping("/dictionary/pages/category/{uuidCategory}")
    public ResponseEntity<?> getDictionaryPagesFromCategory(@PathVariable String uuidCategory) {
        Category subcategory = categoryService.getCategoryByUuid(uuidCategory);
        return ResponseEntity.ok(dictionaryPageService.getDictionaryPagesFromCategory(subcategory.getId()));
    }

    @GetMapping("/admin/new-dictionary-page")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> newDictionaryPage(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/dictionary-pages")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getDictionaryPagesForAdmin(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "30", required = false) int size,
                                                        Principal principal) {
        if (principal != null) {
            if (page < 0) page = 0;
            Page<DictionaryPage> dictionaryPages = dictionaryPageService.getDictionaryPages(page, size);
            int totalPages = dictionaryPages.getTotalPages();
            if (dictionaryPages.getTotalPages() == 0) totalPages = 1;
            return ResponseEntity.ok(new GetPaginationEntityPage<>(dictionaryPages.getContent(),
                    totalPages,
                    dictionaryPages.getTotalElements(),
                    dictionaryPages.getNumber()
            ));
        }
        return ResponseEntity.status(403).body("Access denied");
    }


    @GetMapping("/admin/dictionary-page/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getDictionaryPageByUuid(@PathVariable String uuid, Principal principal) {
        if (principal != null) {
            List<Category> mainCategories = categoryService.getMainCategories(true);
            try {
                DictionaryPage dictionaryPage = dictionaryPageService.getDictionaryPageByUuid(uuid);
                return ResponseEntity.ok(new GetEntityAndMainCategoriesResponse<>(dictionaryPage, mainCategories));
            } catch (ObjectNotFoundException e) {
                DictionaryPage newDictionaryPage = dictionaryPageService.getNewDictionaryPage(uuid);
                return ResponseEntity.ok(new GetEntityAndMainCategoriesResponse<>(newDictionaryPage, mainCategories));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @PutMapping("/admin/dictionary-page/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> dictionaryPageSave(@PathVariable String uuid,
                                                @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                @Valid @RequestPart(value = "dictionaryPage") DictionaryPage dictionaryPage,
                                                BindingResult bindingResult,
                                                Principal principal) throws IOException {
        if (principal != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(validateFields.bindingResultMessages(bindingResult));
            }
            if (dictionaryPage.getWord() == null || dictionaryPage.getWord().getId() == null) {
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_REQUIRED_FIELD));
            }
            try {
                DictionaryPage dictionaryPageDB = dictionaryPageService.getDictionaryPageByUuid(uuid);
                if (!dictionaryPage.getWord().getId().equals(dictionaryPageDB.getWord().getId()) && dictionaryPageService.existDictionaryPageByName(dictionaryPage.getWord().getName())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT));
                }
                dictionaryPage.setImage(new Image());
                if (imageFile != null) {
                    dictionaryPage.getImage().setImageName(fileStorageService.storeFile(imageFile, wordStorePath, dictionaryPage.getWord().getName()));
                    if (dictionaryPageDB.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(dictionaryPageDB.getImage().getImageName(), wordStorePath);
                }
                return ResponseEntity.ok(dictionaryPageService.saveDictionaryPage(dictionaryPageDB, dictionaryPage));

            } catch (ObjectNotFoundException e) {
                if (dictionaryPageService.existDictionaryPageByName(dictionaryPage.getWord().getName())) {
                    return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_DUPLICATE_TEXT));
                }
                Image image = new Image();
                if (imageFile != null)
                    image.setImageName(fileStorageService.storeFile(imageFile, wordStorePath, dictionaryPage.getWord().getName()));
                dictionaryPage.setImage(image);
                return ResponseEntity.ok(dictionaryPageService.saveNewDictionaryPage(dictionaryPage));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/search-dictionary-page-for-lesson")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @JsonView(JsonViews.ViewIdAndName.class)
    public ResponseEntity<?> searchWordForDictionaryPage(@RequestParam("searchTerm") String searchTerm, Principal principal) {
        if (!searchTerm.isBlank() && principal != null) {
            List<DictionaryPage> dictionaryPageList = dictionaryPageService.searchDictionaryPageForWordLesson(searchTerm);
            return ResponseEntity.ok(dictionaryPageList);
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @PostMapping("/dictionary-page/{id}/verify-user-word")
    public ResponseEntity<CustomResponseMessage> wordConfirm(@PathVariable("id") long vocabularyPageId,
                                                             @RequestParam(name = "userWord") String userWord,
                                                             Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(dictionaryPageService.verifyUserWord(userWord, vocabularyPageId));
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
