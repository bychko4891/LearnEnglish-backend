package top.e_learn.learnEnglish.config;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.user.UserService;
import top.e_learn.learnEnglish.service.UserStatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.util.regex.Pattern;

public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private UserService userService;


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && Pattern.matches("REQUEST : (GET|POST) /lesson/[0-9]+.+", message)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                long userId = ((User) principal).getId();
//                userStatisticsService.learnUserTime(userId);
            }
        } else if (authentication != null && authentication.isAuthenticated() && Pattern.matches("REQUEST : GET /user/[0-9]+.+", message)){
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                long userId = ((User) principal).getId();
                String ipAddress = request.getRemoteAddr();
                userService.saveUserIp(userId, ipAddress);
                System.out.println("IP Address юзера: " + ipAddress);
            }
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
//        System.out.println(request + " afterRequest **********************************88");
        // Викликається після обробки запиту
        // Тут ви можете зберігати час закінчення запиту
    }
}
