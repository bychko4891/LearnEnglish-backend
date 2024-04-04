package top.e_learn.learnEnglish.user;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.e_learn.learnEnglish.fileStorage.FileStorageService;
import top.e_learn.learnEnglish.payload.request.ForgotPasswordRequest;
import top.e_learn.learnEnglish.payload.request.SignupRequest;
import top.e_learn.learnEnglish.payload.response.UserJwtForLoginResponse;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.security.jwt.JwtUtils;
import top.e_learn.learnEnglish.service.*;
import top.e_learn.learnEnglish.utils.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class UserController {

    @Value("${file.upload-user-avatar}")
    private String userAvatarStorePath;
//    Locale currentLocale = LocaleContextHolder.getLocale();

    private final UserService userService;

    private final WordUserService wordUserService;

    private final PhraseAndUserService phraseAndUserService;

    private final UserWordLessonProgressService userWordLessonProgressService;

    private final HttpSession session;

    private final FileStorageService fileStorageService;

    private final ValidateFields validate;

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    private final MessageSource messageSource;

    @PostMapping("/api/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          BindingResult bindingResult) throws MessagingException {

        Locale currentLocale = LocaleContextHolder.getLocale();
        List<CustomFieldError> errorFields = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResultMessages(bindingResult));
        }

        if (validate.existsByEmail(signUpRequest.getEmail())) {
            errorFields.add(new CustomFieldError("email", messageSource.getMessage("email.duplicate", null, currentLocale)));
            return ResponseEntity.badRequest().body(ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields));
        }
        userService.createUser(signUpRequest);
        return ResponseEntity.ok(new CustomFieldError("general", messageSource.getMessage("user.signup.success", null, currentLocale)));
    }


    @GetMapping("/api/validate-email/{code}")
    public ResponseEntity<?> verificationUserEmail(@PathVariable("code") String code) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        Optional<User> optionalUser = userService.verificationUserEmail(code);
        if (optionalUser.isPresent()) {
            Authentication authentication = userService.userAuthentication(optionalUser.get().getEmail());

            final String jwtAccessToken = jwtUtils.generateJwtAccessToken(authentication);
            final String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);
            authService.saveJwtRefreshTokenToStorage(optionalUser.get().getEmail(), jwtRefreshToken);

            UserJwtForLoginResponse response = new UserJwtForLoginResponse(
                    jwtAccessToken,
                    jwtRefreshToken
            );
            return ResponseEntity.ok(response);
        } else {
            CustomFieldError customFieldError = new CustomFieldError("general", messageSource.getMessage("user.bad.enable.account", null, currentLocale));
            return ResponseEntity.badRequest().body(ParserToResponseFromCustomFieldError.parseCustomFieldError(customFieldError));
        }
    }

    @GetMapping("/api/user/profile")
    @JsonView(JsonViews.ViewUserProfile.class)
    public ResponseEntity<?> getUserProfile(Principal principal) {
        if (principal != null) {
            User user = userService.getUserFromSecurityContextHolder();
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Error principal");
    }

    @GetMapping("/api/user/{uuid}")
    @JsonView(JsonViews.ViewUserProfile.class)
    public ResponseEntity<?> getUserInfo(@PathVariable String uuid) {
        User user = userService.getUserByUUID(uuid);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/forgot-password")
    public ResponseEntity<?> forgotPasswordStepOne(@Valid @RequestBody ForgotPasswordRequest email, BindingResult bindingResult) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResultMessages(bindingResult));
        }
        boolean emailIsPresent = userService.forgotPasswordStepOne(email);
        if (emailIsPresent) {
            String success = messageSource.getMessage("user.success.email.forgot.password", null, currentLocale);
            return ResponseEntity.ok(new MessageResponse(success));
        }
        String error = messageSource.getMessage("user.bad.email.forgot.password", null, currentLocale);
        MessageResponse message = new MessageResponse(error);
        return ResponseEntity.status(404).body(message);
    }

    @PostMapping("/user/{id}/user-text-check")
    public ResponseEntity<CustomResponseMessage> mytext(@PathVariable("id") Long userId,
                                                        @RequestParam("userActive") boolean isChecked,
                                                        Principal principal) {
        if (principal != null) {
            session.setAttribute("userTextInLesson", isChecked);
            return ResponseEntity.ok(userService.setUserTextInLesson(userId, isChecked));
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/api/user/edit")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> setUserInfo(@RequestParam(value = "firstName", required = false) String firstName,
                                              @RequestParam(value = "lastName", required = false) String lastName,
                                              @RequestParam(value = "gender", required = false) String gender,
                                              Principal principal) {
//        if (principal != null) {
//            userId = userService.findByEmail(principal.getName()).getId();
//            User user = userService.findByEmail(principal.getName());
//            userService.updateUserInfo(userId, firstName, lastName, gender);
//            session.setAttribute("userLogin", firstName);
//            session.setAttribute("userName", lastName);
//            session.setAttribute("userGender", "[" + gender + "]");
//            return ResponseEntity.ok("Інформація успішно оновлена");
//        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/user/update-password")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> setUserPassword(@PathVariable("userId") Long userId,
                                                                 @RequestParam(value = "password") String oldPassword,
                                                                 @RequestParam(value = "newPassword") String newPassword,
                                                                 Principal principal) {
        if (principal != null) {
//            userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(userService.updateUserPassword(userId, oldPassword, newPassword));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<CustomResponseMessage> userProfileRemove(@RequestParam("password") String userPassword,
                                                                   Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(userService.userProfileDelete(user, userPassword));
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/user/word-plus")
    public ResponseEntity<CustomResponseMessage> wordUserPlus(@RequestParam("wordId") Long wordId,
                                                              Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(wordUserService.userWordPlus(user, wordId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user-word/remove")
    public ResponseEntity<CustomResponseMessage> userWordRemove(@RequestParam("wordId") Long wordId,
                                                                Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(wordUserService.userWordRemove(wordId, user));
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/phrase/repetition-phrase-check")
    public ResponseEntity<CustomResponseMessage> isRepetitionPhrase(@RequestParam("isRepeatable") boolean isChecked,
                                                                    @RequestParam("translationPairsId") Long id,
                                                                    Principal principal) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(phraseAndUserService.setRepetitionPhrase(id, userId, isChecked));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word/repetition-word-check")
    public ResponseEntity<CustomResponseMessage> isRepetitionWord(@RequestParam("isRepeatable") boolean isChecked,
                                                                  @RequestParam("wordId") Long id,
                                                                  Principal principal) {
        if (principal != null) {
            Long userId = userService.findByEmail(principal.getName()).getId();
            return ResponseEntity.ok(wordUserService.setRepetitionWord(id, userId, isChecked));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user-phrase/remove")
    public ResponseEntity<CustomResponseMessage> userPhraseRemove(@RequestParam("phraseId") Long translationPairId,
                                                                  Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            return ResponseEntity.ok(phraseAndUserService.userPhraseRemove(translationPairId, user));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/word-lesson/{wordLessonId}/start-lesson/save")
    public ResponseEntity<String> wordLessonStart(@PathVariable long wordLessonId, @RequestParam("start") boolean start,
                                                  Principal principal) {
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            userWordLessonProgressService.startWordLesson(user, wordLessonId, start);
            return ResponseEntity.ok("tab2");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/upload-image-avatar")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CustomResponseMessage> uploadFile(@RequestParam("imageFile") MultipartFile imageFile,
                                                            Principal principal) throws IOException {
        if (principal != null && imageFile != null) {
            String contentType = imageFile.getContentType();
            if (contentType.equals("image/png")) {
                User user = userService.findByEmail(principal.getName());
                String imageFileName = fileStorageService.storeFile(imageFile, userAvatarStorePath, user.getName());
                session.setAttribute("avatarName", imageFileName);
                if (user.getUserAvatar().getImageName() != null && !user.getUserAvatar().getImageName().equalsIgnoreCase("no-avatar.png")) {
                    fileStorageService.deleteFileFromStorage(user.getUserAvatar().getImageName(), userAvatarStorePath);
                }
                user.getUserAvatar().setImageName(imageFileName);
                userService.saveUser(user);
                return ResponseEntity.ok(new CustomResponseMessage(Message.SUCCESS_UPLOAD_USER_AVATAR));
            } else
                return ResponseEntity.ok(new CustomResponseMessage(Message.ERROR_UPLOAD_USER_AVATAR, "Дозволено тільки файли з розширенням .png"));
        }
        return ResponseEntity.notFound().build();
    }

    private Map<String, String> bindingResultMessages(BindingResult bindingResult) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        List<CustomFieldError> errorFields = new ArrayList<>();
        try {
            errorFields = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new CustomFieldError(fieldError.getField(), messageSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale)))
                    .collect(Collectors.toList());
            return ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields);
        } catch (NoSuchMessageException e) {
            errorFields.clear();
            errorFields.add(new CustomFieldError("serverError", messageSource.getMessage("server.error", null, currentLocale)));
            return ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields);
        }
    }


}

