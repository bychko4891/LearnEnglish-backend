package top.e_learn.learnEnglish.utils;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import top.e_learn.learnEnglish.applicationPage.ApplicationPageService;
import top.e_learn.learnEnglish.user.UserRepository;
import top.e_learn.learnEnglish.utils.dto.PhraseUserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ValidateFields {

    private final UserRepository userRepository;

    private final MessageSource messageSource;


    public boolean existsByEmail (String email) {
        return userRepository.existsByEmail(email);
    }

    public static boolean isSupportedImageType(String contentType) {
        return contentType != null && (contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/png")
                || contentType.equalsIgnoreCase("image/webp") || contentType.equalsIgnoreCase("image/jpg"));
    }

    public static boolean validateTranslationPairs(PhraseUserDto phraseUserDto) {

        if (Pattern.matches
                ("(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{4,20}\\b$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)|" +
                        "(^\\b[а-яА-Я[іїєІЇЄ]['`][-]]{1,20}\\b\\,?)\\s{1}(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b\\,?\\s{1})+(\\b[а-яА-Я [іїєІЇЄ]['`][-]]{1,20}\\b[.?!]?$)", phraseUserDto.getUkrTranslate()) &&

                Pattern.matches
                        ("(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z ' `]{1,20}\\b[. ! ?]?$)|" +
                                "(^\\b[a-zA-Z['`]]{1,20}\\b\\,?)\\s{1}(\\b[a-zA-Z['`]]{1,20}\\b\\,?\\s{1})+(\\b[a-zA-Z['`]]{1,20}\\b[.!?]?$)", phraseUserDto.getEngPhrase())) {
            return true;
        }
        return false;
    }

    public Map<String, String> bindingResultMessages(BindingResult bindingResult) {
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
