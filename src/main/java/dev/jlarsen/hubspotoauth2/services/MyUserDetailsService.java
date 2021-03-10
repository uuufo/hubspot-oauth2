package dev.jlarsen.hubspotoauth2.services;

import dev.jlarsen.hubspotoauth2.models.Role;
import dev.jlarsen.hubspotoauth2.models.User;
import dev.jlarsen.hubspotoauth2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        User user = userService.getUser(email);
        List<GrantedAuthority> authorities = getUserAuthorities(user.getRoles());
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                user.isEnabled(), true, true, true, authorities );
    }

    private List<GrantedAuthority> getUserAuthorities(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (Role role : userRoles) {
            roles.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }
        return new ArrayList<>(roles);
    }
}
