package top.e_learn.learnEnglish.service;

import top.e_learn.learnEnglish.security.UserDetailsImpl;
import top.e_learn.learnEnglish.security.jwt.JwtUtils;
import top.e_learn.learnEnglish.model.users.User;
import top.e_learn.learnEnglish.payload.response.JwtResponse;
import top.e_learn.learnEnglish.utils.exception.BadRefreshTokenException;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final Map<String, String> refreshStorage = new HashMap<>();

    public void saveJwtRefreshTokenToStorage(String userEmail, String jwtRefreshToken) {
        refreshStorage.remove(userEmail);
        refreshStorage.put(userEmail, jwtRefreshToken);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException, BadRefreshTokenException {
        if (jwtUtils.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtUtils.getRefreshClaims(refreshToken);
            final String userEmail = claims.getSubject();
//            Integer id = (Integer)claims.get("id"); // Id User
            final String saveRefreshToken = refreshStorage.get(userEmail);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Authentication authentication = userService.userAuthentication(userEmail);
                if (authentication == null) throw new AuthException("User authentication error");

                final String accessToken = jwtUtils.generateJwtAccessToken(authentication);

                return new JwtResponse(accessToken, null);
            }
        }
        throw new BadRefreshTokenException("It is not correct refresh token");
    }


    public JwtResponse getJwtRefreshToken(@NonNull String refreshToken) throws AuthException, BadRefreshTokenException {
        if (jwtUtils.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtUtils.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final Authentication authentication = userService.userAuthentication(login);
//                final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) throw new AuthException("User authentication error");

                final String accessToken = jwtUtils.generateJwtAccessToken(authentication);
                final String newRefreshToken = jwtUtils.generateRefreshToken(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                refreshStorage.put(userDetails.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
//            }
        }
        throw new BadRefreshTokenException("It is not correct refresh token");
    }

    public boolean userSearchByRefreshStorage(String userEmail) {
        return  refreshStorage.get(userEmail) != null;

    }

    public void userDeleteByRefreshStorage(String userEmail) {
        refreshStorage.remove(userEmail);

    }

}

