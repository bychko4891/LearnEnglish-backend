package top.e_learn.learnEnglish.utils;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.ApplicationPage;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.model.WayForPayModule;
import top.e_learn.learnEnglish.model.users.Role;
import top.e_learn.learnEnglish.model.users.User;
import top.e_learn.learnEnglish.model.users.UserGender;
import top.e_learn.learnEnglish.model.users.UserStatistics;
import top.e_learn.learnEnglish.repository.UserRepository;
import top.e_learn.learnEnglish.service.PageApplicationService;
import top.e_learn.learnEnglish.service.WayForPayModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {

    @Value(("${user.admin.email}"))
    private String adminEmail;

    @Value(("${user.admin.password}"))
    private String adminPassword;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PageApplicationService pageApplicationService;

    private final WayForPayModuleService wayForPayModuleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setLogin("Admin");
            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setActive(true);
            admin.setUserPhrasesInLesson(false);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.getUserRole().add(Role.ROLE_ADMIN);
            admin.getGender().add(UserGender.MALE);
            UserStatistics userStatistics = new UserStatistics();
            userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
            userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
            admin.setStatistics(userStatistics);
            Image image = new Image();
            image.setImageName("no-avatar.png");
            admin.setUserAvatar(image);
            userRepository.save(admin);
            createUserDemo();
            createPageApplication();
            createWayForPayModule();
        }
    }

    public void createUserDemo() {
        User demo = new User();
        demo.setLogin("Demo");
        demo.setName("Demo");
        demo.setEmail("demo@mail.com");
        demo.setActive(true);
        demo.setUserPhrasesInLesson(false);
        demo.setPassword(passwordEncoder.encode("demo"));
        demo.getUserRole().add(Role.ROLE_DEMO);
        demo.getGender().add(UserGender.MALE);
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
        userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
        demo.setStatistics(userStatistics);
        Image image = new Image();
        image.setImageName("no-avatar.png");
        demo.setUserAvatar(image);
        userRepository.save(demo);

    }

    private void createPageApplication() {
        ApplicationPage applicationPageMainTop = new ApplicationPage(1L,"/");
        pageApplicationService.savePageApplication(applicationPageMainTop);
        ApplicationPage applicationPageAboutTheApp = new ApplicationPage(2L,"/about");
        pageApplicationService.savePageApplication(applicationPageAboutTheApp);
        ApplicationPage applicationPagePolicy = new ApplicationPage(3L,"Policy","/policy-condition");
        pageApplicationService.savePageApplication(applicationPagePolicy);
        ApplicationPage applicationPageCookie = new ApplicationPage(4L,"Cookie","/coolie");
        pageApplicationService.savePageApplication(applicationPageCookie);
        ApplicationPage applicationPageUploadUserText = new ApplicationPage(5L,"Mini description phrase lessons","#");
        pageApplicationService.savePageApplication(applicationPageUploadUserText);
    }

    private void createWayForPayModule() {
        WayForPayModule wayForPayModule = new WayForPayModule();
        wayForPayModule.setMerchantAccount("Enter merch");
        wayForPayModule.setMerchantSecretKey("Enter key");
        wayForPayModuleService.saveWayForPayModule(wayForPayModule);
    }
}
