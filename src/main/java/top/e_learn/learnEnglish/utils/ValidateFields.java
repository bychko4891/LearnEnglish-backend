package top.e_learn.learnEnglish.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import top.e_learn.learnEnglish.repository.UserRepository;
import top.e_learn.learnEnglish.utils.dto.PhraseUserDto;

import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class ValidateFields {

    private final UserRepository userRepository;


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
}
