package top.e_learn.learnEnglish.payload.request;

import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(
        @Email(message = "bad.email.field") String email) {

}
