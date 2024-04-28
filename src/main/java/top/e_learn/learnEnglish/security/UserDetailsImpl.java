package top.e_learn.learnEnglish.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.e_learn.learnEnglish.image.Image;
import top.e_learn.learnEnglish.user.Role;
import top.e_learn.learnEnglish.user.User;
import top.e_learn.learnEnglish.user.UserGender;

import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }
    public String getUuid() {
        return user.getUuid();
    }
    public String getName() {
        return user.getName();
    }
    public String getLogin() {
        return user.getLogin();
    }
    public String getEmail() {
        return user.getEmail();
    }
    public Image getUserAvatar() {
        return user.getUserAvatar();
    }
    public Set<UserGender> getUserGender() {
        return user.getGender();
    }
    public Set<Role> getAuthority() {
        return user.getUserRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRole();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnable();
    }
}
