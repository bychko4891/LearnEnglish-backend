package top.e_learn.learnEnglish.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.mail.MessagingException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import top.e_learn.learnEnglish.payload.request.GoogleAuthRequest;
import top.e_learn.learnEnglish.payload.response.UserJwtForLoginResponse;
import top.e_learn.learnEnglish.security.UserDetailsImpl;
import top.e_learn.learnEnglish.security.jwt.JwtUtils;
import top.e_learn.learnEnglish.payload.request.AuthRequest;
import top.e_learn.learnEnglish.payload.response.JwtResponse;
import top.e_learn.learnEnglish.service.AuthService;
import top.e_learn.learnEnglish.user.UserService;
import top.e_learn.learnEnglish.utils.CustomFieldError;
import top.e_learn.learnEnglish.utils.JsonViews;
import top.e_learn.learnEnglish.utils.ParserToResponseFromCustomFieldError;
import top.e_learn.learnEnglish.utils.ValidateFields;
import top.e_learn.learnEnglish.utils.exception.BadRefreshTokenException;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AutController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final AuthService authService;

    private final ValidateFields validate;

    private final UserService userService;

    private final MessageSource messageSource;
    @PostMapping("/login")
    public ResponseEntity<?> userAuth(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (bindingResult.hasErrors()) {
            List<CustomFieldError> errorFields = new ArrayList<>();
            try {
                errorFields = bindingResult.getFieldErrors().stream()
                        .map(fieldError -> new CustomFieldError(fieldError.getField(), messageSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale)))
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields));
            } catch (NoSuchMessageException e) {
                errorFields.clear();
                return ResponseEntity.badRequest().body(ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields));
            }
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwtAccessToken = jwtUtils.generateJwtAccessToken(authentication);
        final String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        authService.saveJwtRefreshTokenToStorage(userDetails.getUsername(), jwtRefreshToken);
        UserJwtForLoginResponse response = new UserJwtForLoginResponse(
                jwtAccessToken,
                jwtRefreshToken
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    @JsonView(JsonViews.ViewFieldJWT.class)
    @CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
    public ResponseEntity<?> googleAuth(@RequestBody GoogleAuthRequest googleAuthRequest) throws MessagingException {
        if (!validate.existsByEmail(googleAuthRequest.getEmail())) {
            userService.createUserGoogleAuth(googleAuthRequest);
        }

        Authentication authentication = userService.userAuthentication(googleAuthRequest.getEmail());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwtAccessToken = jwtUtils.generateJwtAccessToken(authentication);
        final String jwtRefreshToken = jwtUtils.generateRefreshToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        authService.saveJwtRefreshTokenToStorage(userDetails.getUsername(), jwtRefreshToken);
        UserJwtForLoginResponse response = new UserJwtForLoginResponse(
                jwtAccessToken,
                jwtRefreshToken
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh/access-token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtResponse request) throws AuthException, BadRefreshTokenException {
        final JwtResponse token = authService.getAccessToken(request.getJwtRefreshToken());
        return ResponseEntity.ok(token);
    }


    @PostMapping("/refresh/refresh-token")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtResponse request) throws AuthException, BadRefreshTokenException {
        final JwtResponse token = authService.getJwtRefreshToken(request.getJwtRefreshToken());
        return ResponseEntity.ok(token);
    }

}
