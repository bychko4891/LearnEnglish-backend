//package top.e_learn.learnEnglish.controllers;
//
///**
// * @author: Anatolii Bychko
// * Application Name: Learn English
// * Description: My Description
// * GitHub source code: https://github.com/bychko4891/learnenglish
// */
//
//import top.e_learn.learnEnglish.model.UserContextHolder;
//import top.e_learn.learnEnglish.word.Word;
//import top.e_learn.learnEnglish.user.User;
//import top.e_learn.learnEnglish.service.PageApplicationService;
//import top.e_learn.learnEnglish.user.UserService;
//import top.e_learn.learnEnglish.word.WordService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//@Controller
//@RequiredArgsConstructor
//public class UserController {
//    private final UserService userService;
//    private final PageApplicationService pageApplicationService;
//    private final UserContextHolder userContextHolder;
//    private final WordService wordService;
//
//
////    @GetMapping("/registration-page")
////    public String registration(Model model) {
////        model.addAttribute("title", "About the app Learn English");
////        PageApplication pageApplication = pageApplicationService.getPageApplication(2l);
////        if (pageApplication.getContentAppPage() != null) {
////            model.addAttribute("pageText", pageApplication.getContentAppPage().getDescription());
////        } else {
////            model.addAttribute("pageText", "No text in this page");
////        }
////        return "registration";
////    }
//
//
//    @GetMapping("/activate/{code}")
//    public String activateUser(@PathVariable("code") String code, Model model) {
//        boolean active = userService.activateUser(code);
//        if (active) {
//            model.addAttribute("message", "Activate");
//        } else model.addAttribute("message", "No activate");
//        return "login";
//    }
//
//    @GetMapping("/forgot-password")
//    public String forgotPassword() {
//        return "forgotPassword";
//    }
//
//
//    @GetMapping("/user/settings")
//    public String editUserInfo(Principal principal, Model model) {
//        model.addAttribute("title", "About the app Learn English");
//        if (principal != null) {
//            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("user", user);
//            return "userSettings";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/user/statistics")
//    public String userStatisticsPage(Principal principal, Model model) {
//        model.addAttribute("title", "About the app Learn English");
//        if (principal != null) {
//            return "userStatistics";
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/user/words")
//    public String getUserWords(@RequestParam(value = "page", defaultValue = "0") int page,
//                               @RequestParam(value = "size", defaultValue = "10", required = false) int size,
//                               Principal principal,
//                               Model model)  {
//        if (principal != null) {
//            if (page < 0) page = 0;
//            Long userId = userService.findByEmail(principal.getName()).getId();
//            Page<Word> words = wordService.getUserWords(page, size, userId);
//            if (words.getTotalPages() == 0) {
//                model.addAttribute("totalPages", 1);
//            } else {
//                model.addAttribute("totalPages", words.getTotalPages());
//            }
//            model.addAttribute("words",words);
//            model.addAttribute("currentPage", page);
//            return "userWords";
//        } else return "redirect:/login";
//    }
//
//
//}
