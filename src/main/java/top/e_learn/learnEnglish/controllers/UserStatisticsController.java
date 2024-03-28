package top.e_learn.learnEnglish.controllers;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.dto.DtoUserStatisticsToUi;
import top.e_learn.learnEnglish.service.UserService;
import top.e_learn.learnEnglish.service.UserStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserStatisticsController {
    private final UserStatisticsService userStatisticsService;
    private final UserService userService;


    @GetMapping("/user/{userId}/training-days")
    public ResponseEntity<List> getUserCalendarDays(@PathVariable(value = "userId") long userId,
                                                    Principal principal) {
        if (principal != null) {
            userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(userStatisticsService.trainingDays(userId));
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/user/user-statistics")
    public ResponseEntity<DtoUserStatisticsToUi> getTrainingUserStatistics(Principal principal) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
//            UserStatistics userStatistics = userStatisticsService.trainingStatistics(userId);
            return ResponseEntity.ok(userStatisticsService.trainingStatistics(userId));
        }
        return ResponseEntity.notFound().build();
    }
//    @GetMapping("/user/{userId}/training-time")
//    public ResponseEntity<List> trainingsTwoWeeks(@PathVariable(value = "userId") long userId, Principal principal) {
//        if (principal != null) {
//            userId = userService.findByEmail(principal.getName()).getId();
//            return ResponseEntity.ok(userStatisticsService.timeWeeks(userId));
//        }
//        return ResponseEntity.notFound().build();
//    }

}
