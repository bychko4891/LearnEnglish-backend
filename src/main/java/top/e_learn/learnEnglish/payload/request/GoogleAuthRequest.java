package top.e_learn.learnEnglish.payload.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class GoogleAuthRequest {

    @NonNull
    private String email;

    private String name;

    public GoogleAuthRequest() {
    }
}
