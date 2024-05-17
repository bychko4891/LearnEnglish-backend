//package top.e_learn.learnEnglish.controllers;
//
///**
// * @author: Anatolii Bychko
// * Application Name: Learn English
// * Description: My Description
// * GitHub source code: https://github.com/bychko4891/learnenglish
// */
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import top.e_learn.learnEnglish.category.Category;
//import top.e_learn.learnEnglish.model.MiniStory;
//import top.e_learn.learnEnglish.word.Word;
//import top.e_learn.learnEnglish.category.CategoryService;
//import top.e_learn.learnEnglish.service.MiniStoryService;
//import top.e_learn.learnEnglish.service.PageApplicationService;
//import top.e_learn.learnEnglish.word.WordService;
//
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class LearnEnglishController {
//
//    private final PageApplicationService pageApplicationService;
//    private final CategoryService categoryService;
//    private final WordService wordService;
//    private final MiniStoryService miniStoryService;
//
//


//
//
//

//
//
//    @GetMapping("/phrases-categories")
//    public String phrasesMainCategories(Model model) {
//        List<Category> phrasesMainCategories = categoryService.mainTranslationPairsCategoryListUser(true);
//        if (phrasesMainCategories != null) {
//            model.addAttribute("phrasesMainCategories", phrasesMainCategories);
//        }
//        return "miniStoriesMainCategories";
//    }
//
//    @GetMapping("/phrases-page/{id}")
//    public String getTranslationPairsPage(@PathVariable("id")Long id,
//                                          Model model) {
//        MiniStory miniStory = miniStoryService.getTranslationPairsPage(id);
//        model.addAttribute("translationPairsPage", miniStory);
//        if (miniStory.getCategory() != null) {
//            model.addAttribute("category", miniStory.getCategory());
//        }
//        return "phrasesPage";
//    }
//
//    @GetMapping("/phrases-category/{id}/phrases-pages")
//    public String translationPairsPages(@PathVariable("id") Long id,
//                                        @RequestParam(name = "page", defaultValue = "0") int page,
//                                        @RequestParam(name = "size", defaultValue = "10", required = false) int size,
//                                        Model model) {
//            if (page < 0) page = 0;
//            Page<MiniStory> translationPairsPages = miniStoryService.getTranslationPairsPagesToUser(page, size, id);
//            Category category = categoryService.getCategory(id);
//            if (translationPairsPages.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", translationPairsPages.getTotalPages());
//            }
//            model.addAttribute("translationPairsPages", translationPairsPages.getContent());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("categoryId", id);
//            model.addAttribute("categoryName", category.getName());
//
//            return "phrasesPages";
//
//    }

//}
