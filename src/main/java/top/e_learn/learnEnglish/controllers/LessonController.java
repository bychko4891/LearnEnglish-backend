//package top.e_learn.learnEnglish.controllers;
//
///**
// * @author: Anatolii Bychko
// * Application Name: Learn English
// * Description: My Description
// *  GitHub source code: https://github.com/bychko4891/learnenglish
// */
//
//import top.e_learn.learnEnglish.utils.dto.DtoTranslationPairToUI;
//import top.e_learn.learnEnglish.user.User;
//import top.e_learn.learnEnglish.service.TranslationPairService;
//import top.e_learn.learnEnglish.service.PhraseUserService;
//import top.e_learn.learnEnglish.user.UserService;
//import top.e_learn.learnEnglish.service.UserStatisticsService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//
//
//@RestController
//@RequiredArgsConstructor
//public class LessonController {
//    private final HttpSession session;
//    private final TranslationPairService translationPairService;
//
//    private final PhraseUserService validationTranslationPair;
//    private final UserStatisticsService userStatisticsService;
//    private final UserService userService;
//
//
//    @GetMapping(path = "/lesson/{lessonId}/reload")
//    public ResponseEntity<DtoTranslationPairToUI> randomTranslationPairToLesson(@PathVariable(value = "lessonId") long lessonId,
//                                                                                Principal principal) {
//        if (principal != null) {
//            User user = userService.findByEmail(principal.getName());
//            String userGender = (String) session.getAttribute("userGender");
//            DtoTranslationPairToUI dtoTranslationPairToUI = translationPairService.getDtoTranslationPair(user, lessonId, userGender);
//            session.setAttribute("ukrText", dtoTranslationPairToUI.getUkrText());
//            session.setAttribute("engText", dtoTranslationPairToUI.getEngText());
//            session.setAttribute("fragment", dtoTranslationPairToUI.getFragment());
//            session.setAttribute("engTextCheck", dtoTranslationPairToUI.getEngText().replaceAll("\\?+", ""));
//            session.setAttribute("ukrTextCheck", dtoTranslationPairToUI.getUkrText().replaceAll("\\?+", ""));
//            return ResponseEntity.ok(dtoTranslationPairToUI);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    @GetMapping(path = "/lesson/{lessonId}/check")
//    public ResponseEntity<String> textCheck(@PathVariable(value = "lessonId") long lessonId,
//                                            @RequestParam("textCheck") String text,
//                                            Principal principal) {
//        if (principal != null) {
//            String textCheck = StringUtils.normalizeSpace(text);
//            textCheck = textCheck.replaceAll("[.,!~?$#@*+;%№=/><\\\\^]+", " ").replaceAll("\\s+", " ").trim();
//            if (textCheck.equalsIgnoreCase((String) session.getAttribute("ukrTextCheck")) || textCheck.equalsIgnoreCase((String) session.getAttribute("engTextCheck"))) {
//                return ResponseEntity.ok("Чудово, гарна робота");
//            } else {
//                userStatisticsService.errorUserRepetitionCount(userService.findByEmail(principal.getName()).getId());
//                if (session.getAttribute("fragment").equals("Fragment 1") || session.getAttribute("fragment").equals("Fragment 4")) {
//                    return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
//                            + session.getAttribute("engText") + "</p>");
//                } else
//                    return ResponseEntity.ok("<span style=\"color: #e03e2d;\">Ви допустили помилку.&nbsp; </span>Значення: <br>" + "<p>"
//                            + session.getAttribute("ukrText") + "</p>");
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//
//
//
//}
