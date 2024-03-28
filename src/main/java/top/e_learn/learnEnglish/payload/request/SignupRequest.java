package top.e_learn.learnEnglish.payload.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    @NotNull( message = "login.minsize")
    @Size(min = 3, message = "login.minsize")
    @Size(max = 50, message = "login.maxsize")
    private String name;

    @NotBlank(message = "email.notblank")
    @Size(max = 50, message = "email.maxsize")
    @Email(message = "bad.email.field", regexp = "^(?=.{1,50}$)[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$")
    private String email;

    @Size(min = 6, message = "password.minsize")
    @Size(max = 72, message = "password.maxsize")
    @Pattern(regexp = "^\\S*$", message = "password.spaces")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@<>'$^+#~!%*=?/;:_&|()+-])[A-Za-z\\d@<>'$^+#~!%*=?/;:_&|()+-]+$", message = "password.pattern")
    private String password;

}