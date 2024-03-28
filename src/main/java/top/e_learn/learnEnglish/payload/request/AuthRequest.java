package top.e_learn.learnEnglish.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotNull( message = "login.minsize")
    @Size(max = 50, message = "login.maxsize")
    private String email;

    @NotBlank(message = "password.minsize")
    @Size(max = 72, message = "password.maxsize")
    private String password;

    public AuthRequest() {
    }
}
