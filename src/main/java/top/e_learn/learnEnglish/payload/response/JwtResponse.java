package top.e_learn.learnEnglish.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";

    private String jwtAccessToken;

    private String jwtRefreshToken;

}
