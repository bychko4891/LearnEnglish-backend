package top.e_learn.learnEnglish.payload.response;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.security.UserDetailsImpl;
import top.e_learn.learnEnglish.utils.JsonViews;

@Getter
@Setter
@AllArgsConstructor
public class UserJwtForLoginResponse {


    @JsonView(JsonViews.ViewFieldJWT.class)
    private String jwtAccessToken;

    @JsonView(JsonViews.ViewFieldJWT.class)
    private String jwtRefreshToken;

    @JsonView(JsonViews.ViewFieldUser.class)
    private User user;

    public UserJwtForLoginResponse(String jwtAccessToken, String jwtRefreshToken, UserDetailsImpl userDetails) {
        this.jwtAccessToken = jwtAccessToken;
        this.jwtRefreshToken = jwtRefreshToken;
        this.user = converter(userDetails);

    }

    public UserJwtForLoginResponse(String jwtAccessToken, String jwtRefreshToken) {
        this.jwtAccessToken = jwtAccessToken;
        this.jwtRefreshToken = jwtRefreshToken;
    }

    private User converter(UserDetailsImpl userDetails) {
        User user = new User();
        user.setUuid(userDetails.getUuid());
        user.setName(userDetails.getName());
        user.setLogin(userDetails.getLogin());
        user.setUserAvatar(userDetails.getUserAvatar());
        user.setEmail(userDetails.getEmail());
        user.setGender(userDetails.getUserGender());
        user.setUserRole(userDetails.getAuthority());
        return user;

    }
}
