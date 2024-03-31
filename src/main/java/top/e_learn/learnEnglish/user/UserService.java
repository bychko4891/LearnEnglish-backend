package top.e_learn.learnEnglish.user;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.mail.MessagingException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.thymeleaf.context.Context;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.service.MailSenderService;
import top.e_learn.learnEnglish.service.UserDetailsServiceImpl;
import top.e_learn.learnEnglish.user.statistics.UserStatistics;
import top.e_learn.learnEnglish.payload.request.ForgotPasswordRequest;
import top.e_learn.learnEnglish.payload.request.GoogleAuthRequest;
import top.e_learn.learnEnglish.payload.request.SignupRequest;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Value(("${application.host}"))
    private String host;
    Locale currentLocale = LocaleContextHolder.getLocale();

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailSenderService mailSender;

    private final HttpServletRequest request;

    private final MessageSource messageSource;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public void createUser(SignupRequest signUpRequest) throws MessagingException {
        User user = new User();
        String email = StringUtils.normalizeSpace(signUpRequest.getEmail());
        user.setEmail(email);
        user.setLogin(email.replaceAll("@{1}.+", ""));
        user.setName(signUpRequest.getName());
        user.setUniqueServiceCode(UUID.randomUUID().toString());
        user.getGender().add(UserGender.MALE);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.getUserRole().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
        userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
        user.setStatistics(userStatistics);
        Image image = new Image();
        image.setImageName("no-avatar.png");
        user.setUserAvatar(image);
        userRepository.save(user);
        mailSend(user, "mail.subject.activation", "activation_message_");
    }

    public void createUserGoogleAuth(GoogleAuthRequest request) throws MessagingException {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setActive(true);
        user.setLogin(request.getEmail().replaceAll("@{1}.+", ""));
        user.setName(request.getName());
        user.getGender().add(UserGender.MALE);
        user.getUserRole().add(Role.ROLE_USER);
        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setStudyTimeInTwoWeeks(new ArrayList<>(Arrays.asList(0)));
        userStatistics.setTrainingDaysInMonth(new ArrayList<>(Arrays.asList(LocalDate.now())));
        user.setStatistics(userStatistics);
        Image image = new Image();
        image.setImageName("no-avatar.png");
        user.setUserAvatar(image);
        userRepository.save(user);
        mailSend(user, "mail.subject.activation", "activation_message_");
    }

    public User getUserFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        return userRepository.findByEmail(userDetails.getUsername()).get();
    }


    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {

        }
        return userRepository.findByEmail(email).get();
    }

    public User getUserByUUID(String uuid) {
        return userRepository.findUsersByUuid(uuid)
                .orElseThrow(() -> new ObjectNotFoundException("No User by UUID"));
    }

    public Optional<User> verificationUserEmail(String code) {
        Optional<User> userOptional = userRepository.findUsersByUniqueServiceCode(code);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(true);
//            user.setUniqueServiceCode(null);
            userRepository.save(user);
            return Optional.of(user);
        }
        return userOptional;
    }

    public boolean findUserByServiceCode(String code) {
        Optional<User> userOptional = userRepository.findUsersByUniqueServiceCode(code);
        if (userOptional.isPresent()) {
            return true;
        }
        return false;
    }


    public User getUserById(long id) {
        return userRepository.findById(id).get();
    }

    // доробити !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void updateUserInfo(Long userId, String firstName, String lastName, String gender) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Set<UserGender> newGenders = new HashSet<>();
            newGenders.add(UserGender.valueOf(gender.toUpperCase()));
            User user = optionalUser.get();
            user.setName(lastName);
            user.setLogin(firstName);
            user.getGender().clear();
            user.getGender().add(UserGender.valueOf(gender.toUpperCase()));
            userRepository.save(user);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
//        userRepository.save(user);
    }

    public CustomResponseMessage updateUserPassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encodedPassword = user.getPassword();
            if (passwordEncoder.matches(oldPassword, encodedPassword)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return new CustomResponseMessage(Message.UPDATE_PASSWORD_SUCCESS);
            } else return new CustomResponseMessage(Message.UPDATE_PASSWORD_ERROR);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
//        userRepository.save(user);
    }


    public CustomResponseMessage userProfileDelete(User user, String userPassword) {
        String encodedPassword = user.getPassword();
        if (passwordEncoder.matches(userPassword, encodedPassword)) {
            userRepository.delete(user);
            logoutUser(request);
            return new CustomResponseMessage(Message.UPDATE_PASSWORD_SUCCESS);
        } else return new CustomResponseMessage(Message.UPDATE_PASSWORD_ERROR);


    }

    private void logoutUser(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
    }

    public Page<User> getUsersPage(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return userRepository.findAll(pageable);
        return null;
    }

    public void userActiveEditAdminPage(Long userId, boolean userActive) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            user.setActive(userActive);
            userRepository.save(user);
//            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }
    }

    public void saveUserIp(long userId, String ipAddress) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserIp(ipAddress);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found");
        }

    }

    public boolean forgotPasswordStepOne(ForgotPasswordRequest emailRequest) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(emailRequest.email());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUniqueServiceCode(UUID.randomUUID().toString());
            userRepository.save(user);
            mailSend(user, "mail.subject.forgot.password.step.one", "forgot_password_message_");
            return true;
        }
        return false;
    }


    private void mailSend (User user, String mailSubject, String mailTemplate) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", user.getName());
        context.setVariable("host", host);
        context.setVariable("code", user.getUniqueServiceCode());
        mailSender.sendSimpleMessage(user.getEmail(), messageSource.getMessage(mailSubject, null, currentLocale), mailTemplate + currentLocale.getLanguage(), context);
    }

    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[10];
        secureRandom.nextBytes(randomBytes);
        return Base64.encodeBase64String(randomBytes);
    }


    public boolean activateUser(String code) {
//        User user = userRepository.findByActivationCode(code);
//        if (user == null) {
//            return false;
//        }
//        user.setActive(true);
//        user.setUniqueServiceCode(null);
//        userRepository.save(user);
        return true;
    }

    public CustomResponseMessage setUserTextInLesson(Long userId, boolean isChecked) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserPhrasesInLesson(isChecked);
            userRepository.save(user);
            return new CustomResponseMessage(Message.SUCCESS_CHECKBOX);
        } else throw new IllegalArgumentException("User with id " + userId + " not found");
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Authentication userAuthentication(String userEmail) {
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
