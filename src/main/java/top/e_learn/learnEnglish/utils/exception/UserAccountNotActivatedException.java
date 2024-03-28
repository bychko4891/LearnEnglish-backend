package top.e_learn.learnEnglish.utils.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAccountNotActivatedException extends AuthenticationException {
    public UserAccountNotActivatedException(String message) {
        super(message);
    }
}
