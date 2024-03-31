//package top.e_learn.learnEnglish.controllers;
///**
// * @author: Anatolii Bychko
// * Application Name: Learn English
// * Description: My Description
// * GitHub source code: https://github.com/bychko4891/learnenglish
// */
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import top.e_learn.learnEnglish.model.*;
//import top.e_learn.learnEnglish.user.User;
//import top.e_learn.learnEnglish.service.*;
//
//import java.security.Principal;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin-page")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//@RequiredArgsConstructor
//public class AdminController {
//
//    private final UserService userService;
//    private final TextOfAppPageService textOfAppPageService;
//    private final PageApplicationService pageApplicationService;
//    private final CategoryService categoryService;
//    private final MiniStoryService miniStoryService;
//    private final ImagesService imagesService;
//    private final WordLessonService wordLessonService;
//    private final WayForPayModuleService wayForPayModuleService;
//
//
//    @GetMapping
//    public String adminPage(Principal principal, Model model) {
//        if (principal != null) {
//            model.addAttribute("wayForPaySettings", wayForPayModuleService.getWayForPayModule());
//
//            return "admin/mainAdmin";
//        }
//        return "redirect:/login";
//    }
//

//
//    @GetMapping("/users")
//    public String usersListAdminPage(Model model,
//                                     Principal principal,
//                                     @RequestParam(value = "page", defaultValue = "0") int page,
//                                     @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
//        if (principal != null) {
//            Page<User> userPage = userService.getUsersPage(page, size);
//            model.addAttribute("users", userPage.getContent());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", userPage.getTotalPages());
//            return "admin/adminUsers";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/words-main-page")
//    public String words(Principal principal) {
//        if (principal != null) {
//            return "admin/wordsMainPage";
//        } else return "redirect:/login";
//    }
//
//
//
//
//
//
//    @GetMapping("/phrases-pages")
//    public String translationPairsPages(@RequestParam(value = "page", defaultValue = "0") int page,
//                                        @RequestParam(value = "size", defaultValue = "10", required = false) int size,
//                                        Principal principal,
//                                        Model model) {
//        if (principal != null) {
//            if (page < 0) page = 0;
//            Page<MiniStory> translationPairsPages = miniStoryService.getTranslationPairsPages(page, size);
//            if (translationPairsPages.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", translationPairsPages.getTotalPages());
//            }
//            model.addAttribute("translationPairsPages", translationPairsPages.getContent());
//            model.addAttribute("currentPage", page);
//
//            return "admin/miniStories";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/phrases-pages/new-page-phrases")
//    public String newTranslationPairPage(Principal principal) {
//        if (principal != null) {
//            Long count = miniStoryService.countTranslationPairPages() + 1;
//            return "redirect:/admin-page/phrase/" + count + "/new-page-phrases-create";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/phrase/{id}/new-page-phrases-create")
//    public String newTranslationPairPageCreate(@PathVariable("id") Long id,
//                                               Model model,
//                                               Principal principal) {
//        if (principal != null) {
//            List<Category> mainTranslationPairsPagesCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.MINI_STORIES);
//            if (mainTranslationPairsPagesCategories != null) {
//                model.addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
//            }
//            MiniStory miniStory = new MiniStory();
//            miniStory.setId(id);
//            miniStory.setName("Enter name");
//            miniStory.setStory("Enter text");
//            model.addAttribute("translationPairsPage", miniStory);
//            model.addAttribute("category", "Відсутня");
//
//            return "admin/miniStoriesEdit";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/phrase/{id}/page-phrases-edit")
//    public String newTranslationPairPageEdit(@PathVariable("id") Long id,
//                                             Model model,
//                                             Principal principal) {
//        if (principal != null) {
//            List<Category> mainTranslationPairsPagesCategories = categoryService.getMainCategoryListByCategoryPage(true, CategoryPage.MINI_STORIES);
//            MiniStory miniStory = miniStoryService.getTranslationPairsPage(id);
//            model.addAttribute("category", "Відсутня");
//            if (miniStory.getCategory() != null) {
//                model.addAttribute("category", miniStory.getCategory().getName());
//            }
//
//            if (mainTranslationPairsPagesCategories != null) {
//                model.addAttribute("mainTranslationPairsPagesCategories", mainTranslationPairsPagesCategories);
//            }
//            model.addAttribute("translationPairsPage", miniStory);
//
//
//            return "admin/miniStoriesEdit";
//        }
//        return "redirect:/login";
//    }
//
//
//    @GetMapping("/images")
//    public String imagesPage(@RequestParam(value = "page", defaultValue = "0") int page,
//                             @RequestParam(value = "size", defaultValue = "10", required = false) int size,
//                             Principal principal,
//                             Model model) {
//        if (principal != null) {
//            if (page < 0) page = 0;
//            Page<Image> imagesPage = imagesService.getImages(page, size);
//            if (imagesPage.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", imagesPage.getTotalPages());
//            }
//            model.addAttribute("images", imagesPage.getContent());
//            model.addAttribute("currentPage", page);
//
//            return "admin/webImages";
//        }
//        return "redirect:/login";
//    }
//
//}