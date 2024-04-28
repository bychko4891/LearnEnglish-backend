package top.e_learn.learnEnglish.security;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.security.authentication.BadCredentialsException;
import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.user.UserRepository;
import top.e_learn.learnEnglish.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.utils.exception.UserAccountNotActivatedException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, UserAccountNotActivatedException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("user.bad.authorisation");
        }
        if(!userOptional.get().isEnable()) throw new UserAccountNotActivatedException("email.not.verification");
        User user = userOptional.get();
        return new UserDetailsImpl(user);
    }

}
