package top.e_learn.learnEnglish.utils.exception;

import javax.security.auth.login.LoginException;

public class BadRefreshTokenException extends LoginException {
    public BadRefreshTokenException(String msg) {
        super(msg);
    }
}
